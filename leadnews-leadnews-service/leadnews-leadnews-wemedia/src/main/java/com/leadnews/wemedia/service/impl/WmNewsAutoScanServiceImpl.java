package com.leadnews.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.leadnews.apis.article.IArticleClient;
import com.leadnews.common.aliyun.GreenImageScan;
import com.leadnews.common.aliyun.GreenTextScan;
import com.leadnews.file.service.FileStorageService;
import com.leadnews.model.article.dtos.ArticleDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.pojos.WmChannel;
import com.leadnews.model.wemedia.pojos.WmNews;
import com.leadnews.model.wemedia.pojos.WmUser;
import com.leadnews.wemedia.mapper.WmChannelMapper;
import com.leadnews.wemedia.mapper.WmNewsMapper;
import com.leadnews.wemedia.mapper.WmUserMapper;
import com.leadnews.wemedia.service.WmNewsAutoScanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class WmNewsAutoScanServiceImpl implements WmNewsAutoScanService {

    @Autowired
    private WmNewsMapper wmNewsMapper;

    /**
     * 鑷獟浣撴枃绔犲鏍?
     * @param id 鑷獟浣撴枃绔爄d
     */
    @Override
    @Async  //鏍囨槑褰撳墠鏂规硶鏄竴涓紓姝ユ柟娉?
    public void autoScanWmNews(Integer id) {

//        int a = 1/0;

        //1.鏌ヨ鑷獟浣撴枃绔?
        WmNews wmNews = wmNewsMapper.selectById(id);
        if(wmNews == null){
            throw new RuntimeException("WmNewsAutoScanServiceImpl-鏂囩珷涓嶅瓨鍦?);
        }

        if(wmNews.getStatus().equals(WmNews.Status.SUBMIT.getCode())){
            //浠庡唴瀹逛腑鎻愬彇绾枃鏈唴瀹瑰拰鍥剧墖
            Map<String,Object> textAndImages = handleTextAndImages(wmNews);

            //2.瀹℃牳鏂囨湰鍐呭  闃块噷浜戞帴鍙?
//            boolean isTextScan = handleTextScan((String) textAndImages.get("content"),wmNews);
//            if(!isTextScan)return;

            //3.瀹℃牳鍥剧墖  闃块噷浜戞帴鍙?
//            boolean isImageScan =  handleImageScan((List<String>) textAndImages.get("images"),wmNews);
//            if(!isImageScan)return;

            //4.瀹℃牳鎴愬姛锛屼繚瀛榓pp绔殑鐩稿叧鐨勬枃绔犳暟鎹?
            ResponseResult responseResult = saveAppArticle(wmNews);
            if(!responseResult.getCode().equals(200)){
                throw new RuntimeException("WmNewsAutoScanServiceImpl-鏂囩珷瀹℃牳锛屼繚瀛榓pp绔浉鍏虫枃绔犳暟鎹け璐?);
            }
            //鍥炲～article_id
            wmNews.setArticleId((Long) responseResult.getData());
            updateWmNews(wmNews,(short) 9,"瀹℃牳鎴愬姛");

        }
    }

    @Autowired
    private IArticleClient articleClient;

    @Autowired
    private WmChannelMapper wmChannelMapper;

    @Autowired
    private WmUserMapper wmUserMapper;

    /**
     * 淇濆瓨app绔浉鍏崇殑鏂囩珷鏁版嵁
     * @param wmNews
     */
    private ResponseResult saveAppArticle(WmNews wmNews) {

        ArticleDto dto = new ArticleDto();
        //灞炴€х殑鎷疯礉
        BeanUtils.copyProperties(wmNews,dto);
        //鏂囩珷鐨勫竷灞€
        dto.setLayout(wmNews.getType());
        //棰戦亾
        WmChannel wmChannel = wmChannelMapper.selectById(wmNews.getChannelId());
        if(wmChannel != null){
            dto.setChannelName(wmChannel.getName());
        }

        //浣滆€?
        dto.setAuthorId(wmNews.getUserId().longValue());
        WmUser wmUser = wmUserMapper.selectById(wmNews.getUserId());
        if(wmUser != null){
            dto.setAuthorName(wmUser.getName());
        }

        //璁剧疆鏂囩珷id
        if(wmNews.getArticleId() != null){
            dto.setId(wmNews.getArticleId());
        }
        dto.setCreatedTime(new Date());

        ResponseResult responseResult = articleClient.saveArticle(dto);
        return responseResult;

    }


    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GreenImageScan greenImageScan;

    /**
     * 瀹℃牳鍥剧墖
     * @param images
     * @param wmNews
     * @return
     */
    private boolean handleImageScan(List<String> images, WmNews wmNews) {

        boolean flag = true;

        if(images == null || images.size() == 0){
            return flag;
        }

        //涓嬭浇鍥剧墖 minIO
        //鍥剧墖鍘婚噸
        images = images.stream().distinct().collect(Collectors.toList());

        List<byte[]> imageList = new ArrayList<>();

        for (String image : images) {
            byte[] bytes = fileStorageService.downLoadFile(image);
            imageList.add(bytes);
        }


        //瀹℃牳鍥剧墖
        try {
            Map map = greenImageScan.imageScan(imageList);
            if(map != null){
                //瀹℃牳澶辫触
                if(map.get("suggestion").equals("block")){
                    flag = false;
                    updateWmNews(wmNews, (short) 2, "褰撳墠鏂囩珷涓瓨鍦ㄨ繚瑙勫唴瀹?);
                }

                //涓嶇‘瀹氫俊鎭? 闇€瑕佷汉宸ュ鏍?
                if(map.get("suggestion").equals("review")){
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "褰撳墠鏂囩珷涓瓨鍦ㄤ笉纭畾鍐呭");
                }
            }

        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    @Autowired
    private GreenTextScan greenTextScan;

    /**
     * 瀹℃牳绾枃鏈唴瀹?
     * @param content
     * @param wmNews
     * @return
     */
    private boolean handleTextScan(String content, WmNews wmNews) {

        boolean flag = true;

        if((wmNews.getTitle()+"-"+content).length() == 0){
            return flag;
        }

        try {
            Map map = greenTextScan.greeTextScan((wmNews.getTitle()+"-"+content));
            if(map != null){
                //瀹℃牳澶辫触
                if(map.get("suggestion").equals("block")){
                    flag = false;
                    updateWmNews(wmNews, (short) 2, "褰撳墠鏂囩珷涓瓨鍦ㄨ繚瑙勫唴瀹?);
                }

                //涓嶇‘瀹氫俊鎭? 闇€瑕佷汉宸ュ鏍?
                if(map.get("suggestion").equals("review")){
                    flag = false;
                    updateWmNews(wmNews, (short) 3, "褰撳墠鏂囩珷涓瓨鍦ㄤ笉纭畾鍐呭");
                }
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        }

        return flag;

    }

    /**
     * 淇敼鏂囩珷鍐呭
     * @param wmNews
     * @param status
     * @param reason
     */
    private void updateWmNews(WmNews wmNews, short status, String reason) {
        wmNews.setStatus(status);
        wmNews.setReason(reason);
        wmNewsMapper.updateById(wmNews);
    }

    /**
     * 1銆備粠鑷獟浣撴枃绔犵殑鍐呭涓彁鍙栨枃鏈拰鍥剧墖
     * 2.鎻愬彇鏂囩珷鐨勫皝闈㈠浘鐗?
     * @param wmNews
     * @return
     */
    private Map<String, Object> handleTextAndImages(WmNews wmNews) {

        //瀛樺偍绾枃鏈唴瀹?
        StringBuilder stringBuilder = new StringBuilder();

        List<String> images = new ArrayList<>();

        //1銆備粠鑷獟浣撴枃绔犵殑鍐呭涓彁鍙栨枃鏈拰鍥剧墖
        if(StringUtils.isNotBlank(wmNews.getContent())){
            List<Map> maps = JSONArray.parseArray(wmNews.getContent(), Map.class);
            for (Map map : maps) {
                if (map.get("type").equals("text")){
                    stringBuilder.append(map.get("value"));
                }

                if (map.get("type").equals("image")){
                    images.add((String) map.get("value"));
                }
            }
        }
        //2.鎻愬彇鏂囩珷鐨勫皝闈㈠浘鐗?
        if(StringUtils.isNotBlank(wmNews.getImages())){
            String[] split = wmNews.getImages().split(",");
            images.addAll(Arrays.asList(split));
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("content",stringBuilder.toString());
        resultMap.put("images",images);
        return resultMap;

    }
}

