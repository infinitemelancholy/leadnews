package com.leadnews.wemedia.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.common.constants.WemediaConstants;
import com.leadnews.common.constants.WmNewsMessageConstants;
import com.leadnews.common.exception.CustomException;
import com.leadnews.model.common.dtos.PageResponseResult;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.wemedia.dtos.WmNewsDto;
import com.leadnews.model.wemedia.dtos.WmNewsPageReqDto;
import com.leadnews.model.wemedia.pojos.WmMaterial;
import com.leadnews.model.wemedia.pojos.WmNews;
import com.leadnews.model.wemedia.pojos.WmNewsMaterial;
import com.leadnews.utils.thread.WmThreadLocalUtil;
import com.leadnews.wemedia.mapper.WmMaterialMapper;
import com.leadnews.wemedia.mapper.WmNewsMapper;
import com.leadnews.wemedia.mapper.WmNewsMaterialMapper;
import com.leadnews.wemedia.service.WmNewsAutoScanService;
import com.leadnews.wemedia.service.WmNewsService;
import com.leadnews.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {

    /**
     * 鏉′欢鏌ヨ鏂囩珷鍒楄〃
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult findList(WmNewsPageReqDto dto) {
        //1.妫€鏌ュ弬鏁?
        //鍒嗛〉妫€鏌?
        dto.checkParam();

        //2.鍒嗛〉鏉′欢鏌ヨ
        IPage page = new Page(dto.getPage(), dto.getSize());
        LambdaQueryWrapper<WmNews> lambdaQueryWrapper = new LambdaQueryWrapper();
        //鐘舵€佺簿纭煡璇?
        if (dto.getStatus() != null) {
            lambdaQueryWrapper.eq(WmNews::getStatus, dto.getStatus());
        }

        //棰戦亾绮剧‘鏌ヨ
        if (dto.getChannelId() != null) {
            lambdaQueryWrapper.eq(WmNews::getChannelId, dto.getChannelId());
        }

        //鏃堕棿鑼冨洿鏌ヨ
        if (dto.getBeginPubDate() != null && dto.getEndPubDate() != null) {
            lambdaQueryWrapper.between(WmNews::getPublishTime, dto.getBeginPubDate(), dto.getEndPubDate());
        }

        //鍏抽敭瀛楃殑妯＄硦鏌ヨ
        if (StringUtils.isNotBlank(dto.getKeyword())) {
            lambdaQueryWrapper.like(WmNews::getTitle, dto.getKeyword());
        }

        //鏌ヨ褰撳墠鐧诲綍浜虹殑鏂囩珷
        lambdaQueryWrapper.eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId());

        //鎸夌収鍙戝竷鏃堕棿鍊掑簭鏌ヨ
        lambdaQueryWrapper.orderByDesc(WmNews::getPublishTime);


        page = page(page, lambdaQueryWrapper);

        //3.缁撴灉杩斿洖
        ResponseResult responseResult = new PageResponseResult(dto.getPage(), dto.getSize(), (int) page.getTotal());
        responseResult.setData(page.getRecords());


        return responseResult;
    }

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    @Autowired
    private WmNewsTaskService wmNewsTaskService;


    /**
     * 鍙戝竷淇敼鏂囩珷鎴栦繚瀛樹负鑽夌
     * @param dto
     * @return
     */
    @Override
    public ResponseResult submitNews(WmNewsDto dto) {

        //0.鏉′欢鍒ゆ柇
        if(dto == null || dto.getContent() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.淇濆瓨鎴栦慨鏀规枃绔?

        WmNews wmNews = new WmNews();
        //灞炴€ф嫹璐?灞炴€у悕璇嶅拰绫诲瀷鐩稿悓鎵嶈兘鎷疯礉
        BeanUtils.copyProperties(dto,wmNews);
        //灏侀潰鍥剧墖  list---> string
        if(dto.getImages() != null && dto.getImages().size() > 0){
            //[1dddfsd.jpg,sdlfjldk.jpg]-->   1dddfsd.jpg,sdlfjldk.jpg
            String imageStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imageStr);
        }
        //濡傛灉褰撳墠灏侀潰绫诲瀷涓鸿嚜鍔?-1
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            wmNews.setType(null);
        }

        saveOrUpdateWmNews(wmNews);

        //2.鍒ゆ柇鏄惁涓鸿崏绋? 濡傛灉涓鸿崏绋跨粨鏉熷綋鍓嶆柟娉?
        if(dto.getStatus().equals(WmNews.Status.NORMAL.getCode())){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //3.涓嶆槸鑽夌锛屼繚瀛樻枃绔犲唴瀹瑰浘鐗囦笌绱犳潗鐨勫叧绯?
        //鑾峰彇鍒版枃绔犲唴瀹逛腑鐨勫浘鐗囦俊鎭?
        List<String> materials =  ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,wmNews.getId());

        //4.涓嶆槸鑽夌锛屼繚瀛樻枃绔犲皝闈㈠浘鐗囦笌绱犳潗鐨勫叧绯伙紝濡傛灉褰撳墠甯冨眬鏄嚜鍔紝闇€瑕佸尮閰嶅皝闈㈠浘鐗?
        saveRelativeInfoForCover(dto,wmNews,materials);

        //瀹℃牳鏂囩珷
//        wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        wmNewsTaskService.addNewsToTask(wmNews.getId(),wmNews.getPublishTime());

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }

    /**
     * 绗竴涓姛鑳斤細濡傛灉褰撳墠灏侀潰绫诲瀷涓鸿嚜鍔紝鍒欒缃皝闈㈢被鍨嬬殑鏁版嵁
     * 鍖归厤瑙勫垯锛?
     * 1锛屽鏋滃唴瀹瑰浘鐗囧ぇ浜庣瓑浜?锛屽皬浜?  鍗曞浘  type 1
     * 2锛屽鏋滃唴瀹瑰浘鐗囧ぇ浜庣瓑浜?  澶氬浘  type 3
     * 3锛屽鏋滃唴瀹规病鏈夊浘鐗囷紝鏃犲浘  type 0
     *
     * 绗簩涓姛鑳斤細淇濆瓨灏侀潰鍥剧墖涓庣礌鏉愮殑鍏崇郴
     * @param dto
     * @param wmNews
     * @param materials
     */
    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {

        List<String> images = dto.getImages();

        //濡傛灉褰撳墠灏侀潰绫诲瀷涓鸿嚜鍔紝鍒欒缃皝闈㈢被鍨嬬殑鏁版嵁
        if(dto.getType().equals(WemediaConstants.WM_NEWS_TYPE_AUTO)){
            //澶氬浘
            if(materials.size() >= 3){
                wmNews.setType(WemediaConstants.WM_NEWS_MANY_IMAGE);
                images = materials.stream().limit(3).collect(Collectors.toList());
            }else if(materials.size() >= 1 && materials.size() < 3){
                //鍗曞浘
                wmNews.setType(WemediaConstants.WM_NEWS_SINGLE_IMAGE);
                images = materials.stream().limit(1).collect(Collectors.toList());
            }else {
                //鏃犲浘
                wmNews.setType(WemediaConstants.WM_NEWS_NONE_IMAGE);
            }

            //淇敼鏂囩珷
            if(images != null && images.size() > 0){
                wmNews.setImages(StringUtils.join(images,","));
            }
            updateById(wmNews);
        }
        if(images != null && images.size() > 0){
            saveRelativeInfo(images,wmNews.getId(),WemediaConstants.WM_COVER_REFERENCE);
        }

    }


    /**
     * 澶勭悊鏂囩珷鍐呭鍥剧墖涓庣礌鏉愮殑鍏崇郴
     * @param materials
     * @param newsId
     */
    private void saveRelativeInfoForContent(List<String> materials, Integer newsId) {
        saveRelativeInfo(materials,newsId,WemediaConstants.WM_CONTENT_REFERENCE);
    }

    @Autowired
    private WmMaterialMapper wmMaterialMapper;

    /**
     * 淇濆瓨鏂囩珷鍥剧墖涓庣礌鏉愮殑鍏崇郴鍒版暟鎹簱涓?
     * @param materials
     * @param newsId
     * @param type
     */
    private void saveRelativeInfo(List<String> materials, Integer newsId, Short type) {
        if(materials!=null && materials.size() > 0){
            //閫氳繃鍥剧墖鐨剈rl鏌ヨ绱犳潗鐨刬d
            List<WmMaterial> dbMaterials = wmMaterialMapper.selectList(Wrappers.<WmMaterial>lambdaQuery().in(WmMaterial::getUrl, materials));

            //鍒ゆ柇绱犳潗鏄惁鏈夋晥
            if(dbMaterials==null || dbMaterials.size() == 0){
                //鎵嬪姩鎶涘嚭寮傚父   绗竴涓姛鑳斤細鑳藉鎻愮ず璋冪敤鑰呯礌鏉愬け鏁堜簡锛岀浜屼釜鍔熻兘锛岃繘琛屾暟鎹殑鍥炴粴
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            if(materials.size() != dbMaterials.size()){
                throw new CustomException(AppHttpCodeEnum.MATERIASL_REFERENCE_FAIL);
            }

            List<Integer> idList = dbMaterials.stream().map(WmMaterial::getId).collect(Collectors.toList());

            //鎵归噺淇濆瓨
            wmNewsMaterialMapper.saveRelations(idList,newsId,type);
        }

    }


    /**
     * 鎻愬彇鏂囩珷鍐呭涓殑鍥剧墖淇℃伅
     * @param content
     * @return
     */
    private List<String> ectractUrlInfo(String content) {
        List<String> materials = new ArrayList<>();

        List<Map> maps = JSON.parseArray(content, Map.class);
        for (Map map : maps) {
            if(map.get("type").equals("image")){
                String imgUrl = (String) map.get("value");
                materials.add(imgUrl);
            }
        }

        return materials;
    }

    @Autowired
    private WmNewsMaterialMapper wmNewsMaterialMapper;

    /**
     * 淇濆瓨鎴栦慨鏀规枃绔?
     * @param wmNews
     */
    private void saveOrUpdateWmNews(WmNews wmNews) {
        //琛ュ叏灞炴€?
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        wmNews.setSubmitedTime(new Date());
        wmNews.setEnable((short)1);//榛樿涓婃灦

        if(wmNews.getId() == null){
            //淇濆瓨
            save(wmNews);
        }else {
            //淇敼
            //鍒犻櫎鏂囩珷鍥剧墖涓庣礌鏉愮殑鍏崇郴
            wmNewsMaterialMapper.delete(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId,wmNews.getId()));
            updateById(wmNews);
        }

    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 鏂囩珷鐨勪笂涓嬫灦
     * @param dto
     * @return
     */
    @Override
    public ResponseResult downOrUp(WmNewsDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鏌ヨ鏂囩珷
        WmNews wmNews = getById(dto.getId());
        if(wmNews == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"鏂囩珷涓嶅瓨鍦?);
        }

        //3.鍒ゆ柇鏂囩珷鏄惁宸插彂甯?
        if(!wmNews.getStatus().equals(WmNews.Status.PUBLISHED.getCode())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"褰撳墠鏂囩珷涓嶆槸鍙戝竷鐘舵€侊紝涓嶈兘涓婁笅鏋?);
        }

        //4.淇敼鏂囩珷enable
        if(dto.getEnable() != null && dto.getEnable() > -1 && dto.getEnable() < 2){
            update(Wrappers.<WmNews>lambdaUpdate().set(WmNews::getEnable,dto.getEnable())
                    .eq(WmNews::getId,wmNews.getId()));

            if(wmNews.getArticleId() != null){
                //鍙戦€佹秷鎭紝閫氱煡article淇敼鏂囩珷鐨勯厤缃?
                Map<String,Object> map = new HashMap<>();
                map.put("articleId",wmNews.getArticleId());
                map.put("enable",dto.getEnable());
                kafkaTemplate.send(WmNewsMessageConstants.WM_NEWS_UP_OR_DOWN_TOPIC, JSON.toJSONString(map));
            }

        }
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}

