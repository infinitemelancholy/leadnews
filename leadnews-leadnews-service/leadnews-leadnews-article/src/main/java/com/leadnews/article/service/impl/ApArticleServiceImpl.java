package com.leadnews.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.article.mapper.ApArticleConfigMapper;
import com.leadnews.article.mapper.ApArticleContentMapper;
import com.leadnews.article.mapper.ApArticleMapper;
import com.leadnews.article.service.ApArticleService;
import com.leadnews.article.service.ArticleFreemarkerService;
import com.leadnews.common.constants.ArticleConstants;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.article.dtos.ArticleDto;
import com.leadnews.model.article.dtos.ArticleHomeDto;
import com.leadnews.model.article.dtos.ArticleInfoDto;
import com.leadnews.model.article.pojos.ApArticle;
import com.leadnews.model.article.pojos.ApArticleConfig;
import com.leadnews.model.article.pojos.ApArticleContent;
import com.leadnews.model.article.vos.HotArticleVo;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.mess.ArticleVisitStreamMess;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle>  implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    private final static  short MAX_PAGE_SIZE = 50;

    /**
     * 鍔犺浇鏂囩珷鍒楄〃
     * @param dto
     * @param type 1 鍔犺浇鏇村   2 鍔犺浇鏈€鏂?
     * @return
     */
    @Override
    public ResponseResult load(ArticleHomeDto dto, Short type) {
        //1.妫€楠屽弬鏁?
        //鍒嗛〉鏉℃暟鐨勬牎楠?
        Integer size = dto.getSize();
        if(size == null || size == 0){
            size = 10;
        }
        //鍒嗛〉鐨勫€间笉瓒呰繃50
        size = Math.min(size,MAX_PAGE_SIZE);


        //鏍￠獙鍙傛暟  -->type
        if(!type.equals(ArticleConstants.LOADTYPE_LOAD_MORE) && !type.equals(ArticleConstants.LOADTYPE_LOAD_NEW)){
            type = ArticleConstants.LOADTYPE_LOAD_MORE;
        }

        //棰戦亾鍙傛暟鏍￠獙
        if(StringUtils.isBlank(dto.getTag())){
            dto.setTag(ArticleConstants.DEFAULT_TAG);
        }

        //鏃堕棿鏍￠獙
        if(dto.getMaxBehotTime() == null)dto.setMaxBehotTime(new Date());
        if(dto.getMinBehotTime() == null)dto.setMinBehotTime(new Date());

        //2.鏌ヨ
        List<ApArticle> articleList = apArticleMapper.loadArticleList(dto, type);
        //3.缁撴灉杩斿洖
        return ResponseResult.okResult(articleList);
    }

    @Autowired
    private ApArticleConfigMapper apArticleConfigMapper;

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    @Lazy
    private ArticleFreemarkerService articleFreemarkerService;

    /**
     * 淇濆瓨app绔浉鍏虫枃绔?
     * @param dto
     * @return
     */
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {

//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //1.妫€鏌ュ弬鏁?
        if(dto == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto,apArticle);

        //2.鍒ゆ柇鏄惁瀛樺湪id
        if(dto.getId() == null){
            //2.1 涓嶅瓨鍦╥d  淇濆瓨  鏂囩珷  鏂囩珷閰嶇疆  鏂囩珷鍐呭

            //淇濆瓨鏂囩珷
            save(apArticle);

            //淇濆瓨閰嶇疆
            ApArticleConfig apArticleConfig = new ApArticleConfig(apArticle.getId());
            apArticleConfigMapper.insert(apArticleConfig);

            //淇濆瓨 鏂囩珷鍐呭
            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.insert(apArticleContent);

        }else {
            //2.2 瀛樺湪id   淇敼  鏂囩珷  鏂囩珷鍐呭

            //淇敼  鏂囩珷
            updateById(apArticle);

            //淇敼鏂囩珷鍐呭
            ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
            apArticleContent.setContent(dto.getContent());
            apArticleContentMapper.updateById(apArticleContent);
        }

        //寮傛璋冪敤 鐢熸垚闈欐€佹枃浠朵笂浼犲埌minio涓?
        articleFreemarkerService.buildArticleToMinIO(apArticle,dto.getContent());


        //3.缁撴灉杩斿洖  鏂囩珷鐨刬d
        return ResponseResult.okResult(apArticle.getId());
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto) {

        //0.妫€鏌ュ弬鏁?
        if (dto == null || dto.getArticleId() == null || dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //{ "isfollow": true, "islike": true,"isunlike": false,"iscollection": true }
        boolean isfollow = false, islike = false, isunlike = false, iscollection = false;

        ApUser user = AppThreadLocalUtil.getUser();
        if(user != null){
            //鍠滄琛屼负
            String likeBehaviorJson = (String) cacheService.hGet("LIKE-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString());
            if(StringUtils.isNotBlank(likeBehaviorJson)){
                islike = true;
            }
            //涓嶅枩娆㈢殑琛屼负
            String unLikeBehaviorJson = (String) cacheService.hGet("UNLIKE-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString());
            if(StringUtils.isNotBlank(unLikeBehaviorJson)){
                isunlike = true;
            }
            //鏄惁鏀惰棌
            String collctionJson = (String) cacheService.hGet("COLLECTION-BEHAVIOR-"+dto.getArticleId(),user.getId().toString());
            if(StringUtils.isNotBlank(collctionJson)){
                iscollection = true;
            }

            //鏄惁鍏虫敞
            Double score = cacheService.zScore("apuser:follow:" + user.getId(), dto.getAuthorId().toString());
            System.out.println(score);
            if(score != null){
                isfollow = true;
            }

        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isfollow", isfollow);
        resultMap.put("islike", islike);
        resultMap.put("isunlike", isunlike);
        resultMap.put("iscollection", iscollection);

        return ResponseResult.okResult(resultMap);
    }

    /**
     * 鍔犺浇鏂囩珷鍒楄〃
     * @param dto
     * @param type      1 鍔犺浇鏇村   2 鍔犺浇鏈€鏂?
     * @param firstPage true  鏄椤? flase 闈為椤?
     * @return
     */
    @Override
    public ResponseResult load2(ArticleHomeDto dto, Short type, boolean firstPage) {
        if(firstPage){
            String jsonStr = cacheService.get(ArticleConstants.HOT_ARTICLE_FIRST_PAGE + dto.getTag());
            if(StringUtils.isNotBlank(jsonStr)){
                List<HotArticleVo> hotArticleVoList = JSON.parseArray(jsonStr, HotArticleVo.class);
                ResponseResult responseResult = ResponseResult.okResult(hotArticleVoList);
                return responseResult;
            }
        }
        return load(dto,type);
    }

    /**
     * 鏇存柊鏂囩珷鐨勫垎鍊? 鍚屾椂鏇存柊缂撳瓨涓殑鐑偣鏂囩珷鏁版嵁
     * @param mess
     */
    @Override
    public void updateScore(ArticleVisitStreamMess mess) {
        //1.鏇存柊鏂囩珷鐨勯槄璇汇€佺偣璧炪€佹敹钘忋€佽瘎璁虹殑鏁伴噺
        ApArticle apArticle = updateArticle(mess);
        //2.璁＄畻鏂囩珷鐨勫垎鍊?
        Integer score = computeScore(apArticle);
        score = score * 3;

        //3.鏇挎崲褰撳墠鏂囩珷瀵瑰簲棰戦亾鐨勭儹鐐规暟鎹?
        replaceDataToRedis(apArticle, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + apArticle.getChannelId());

        //4.鏇挎崲鎺ㄨ崘瀵瑰簲鐨勭儹鐐规暟鎹?
        replaceDataToRedis(apArticle, score, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + ArticleConstants.DEFAULT_TAG);

    }

    /**
     * 鏇挎崲鏁版嵁骞朵笖瀛樺叆鍒皉edis
     * @param apArticle
     * @param score
     * @param s
     */
    private void replaceDataToRedis(ApArticle apArticle, Integer score, String s) {
        String articleListStr = cacheService.get(s);
        if (StringUtils.isNotBlank(articleListStr)) {
            List<HotArticleVo> hotArticleVoList = JSON.parseArray(articleListStr, HotArticleVo.class);

            boolean flag = true;

            //濡傛灉缂撳瓨涓瓨鍦ㄨ鏂囩珷锛屽彧鏇存柊鍒嗗€?
            for (HotArticleVo hotArticleVo : hotArticleVoList) {
                if (hotArticleVo.getId().equals(apArticle.getId())) {
                    hotArticleVo.setScore(score);
                    flag = false;
                    break;
                }
            }

            //濡傛灉缂撳瓨涓笉瀛樺湪锛屾煡璇㈢紦瀛樹腑鍒嗗€兼渶灏忕殑涓€鏉℃暟鎹紝杩涜鍒嗗€肩殑姣旇緝锛屽鏋滃綋鍓嶆枃绔犵殑鍒嗗€煎ぇ浜庣紦瀛樹腑鐨勬暟鎹紝灏辨浛鎹?
            if (flag) {
                if (hotArticleVoList.size() >= 30) {
                    hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
                    HotArticleVo lastHot = hotArticleVoList.get(hotArticleVoList.size() - 1);
                    if (lastHot.getScore() < score) {
                        hotArticleVoList.remove(lastHot);
                        HotArticleVo hot = new HotArticleVo();
                        BeanUtils.copyProperties(apArticle, hot);
                        hot.setScore(score);
                        hotArticleVoList.add(hot);
                    }


                } else {
                    HotArticleVo hot = new HotArticleVo();
                    BeanUtils.copyProperties(apArticle, hot);
                    hot.setScore(score);
                    hotArticleVoList.add(hot);
                }
            }
            //缂撳瓨鍒皉edis
            hotArticleVoList = hotArticleVoList.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
            cacheService.set(s, JSON.toJSONString(hotArticleVoList));

        }
    }

    /**
     * 鏇存柊鏂囩珷琛屼负鏁伴噺
     * @param mess
     */
    private ApArticle updateArticle(ArticleVisitStreamMess mess) {
        ApArticle apArticle = getById(mess.getArticleId());
        apArticle.setCollection(apArticle.getCollection()==null?0:apArticle.getCollection()+mess.getCollect());
        apArticle.setComment(apArticle.getComment()==null?0:apArticle.getComment()+mess.getComment());
        apArticle.setLikes(apArticle.getLikes()==null?0:apArticle.getLikes()+mess.getLike());
        apArticle.setViews(apArticle.getViews()==null?0:apArticle.getViews()+mess.getView());
        updateById(apArticle);
        return apArticle;

    }

    /**
     * 璁＄畻鏂囩珷鐨勫叿浣撳垎鍊?
     * @param apArticle
     * @return
     */
    private Integer computeScore(ApArticle apArticle) {
        Integer scere = 0;
        if(apArticle.getLikes() != null){
            scere += apArticle.getLikes() * ArticleConstants.HOT_ARTICLE_LIKE_WEIGHT;
        }
        if(apArticle.getViews() != null){
            scere += apArticle.getViews();
        }
        if(apArticle.getComment() != null){
            scere += apArticle.getComment() * ArticleConstants.HOT_ARTICLE_COMMENT_WEIGHT;
        }
        if(apArticle.getCollection() != null){
            scere += apArticle.getCollection() * ArticleConstants.HOT_ARTICLE_COLLECTION_WEIGHT;
        }

        return scere;
    }
}

