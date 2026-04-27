package com.leadnews.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.apis.wemedia.IWemediaClient;
import com.leadnews.article.mapper.ApArticleMapper;
import com.leadnews.article.service.HotArticleService;
import com.leadnews.common.constants.ArticleConstants;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.article.pojos.ApArticle;
import com.leadnews.model.article.vos.HotArticleVo;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.pojos.WmChannel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HotArticleServiceImpl implements HotArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    /**
     * 璁＄畻鐑偣鏂囩珷
     */
    @Override
    public void computeHotArticle() {
        //1.鏌ヨ鍓?澶╃殑鏂囩珷鏁版嵁
        Date dateParam = DateTime.now().minusDays(50).toDate();
        List<ApArticle> apArticleList = apArticleMapper.findArticleListByLast5days(dateParam);

        //2.璁＄畻鏂囩珷鐨勫垎鍊?
        List<HotArticleVo> hotArticleVoList = computeHotArticle(apArticleList);

        //3.涓烘瘡涓閬撶紦瀛?0鏉″垎鍊艰緝楂樼殑鏂囩珷
        cacheTagToRedis(hotArticleVoList);

    }

    @Autowired
    private IWemediaClient wemediaClient;

    @Autowired
    private CacheService cacheService;

    /**
     * 涓烘瘡涓閬撶紦瀛?0鏉″垎鍊艰緝楂樼殑鏂囩珷
     * @param hotArticleVoList
     */
    private void cacheTagToRedis(List<HotArticleVo> hotArticleVoList) {
        //姣忎釜棰戦亾缂撳瓨30鏉″垎鍊艰緝楂樼殑鏂囩珷
        ResponseResult responseResult = wemediaClient.getChannels();
        if(responseResult.getCode().equals(200)){
            String channelJson = JSON.toJSONString(responseResult.getData());
            List<WmChannel> wmChannels = JSON.parseArray(channelJson, WmChannel.class);
            //妫€绱㈠嚭姣忎釜棰戦亾鐨勬枃绔?
            if(wmChannels != null && wmChannels.size() > 0){
                for (WmChannel wmChannel : wmChannels) {
                    List<HotArticleVo> hotArticleVos = hotArticleVoList.stream().filter(x -> x.getChannelId().equals(wmChannel.getId())).collect(Collectors.toList());
                    //缁欐枃绔犺繘琛屾帓搴忥紝鍙?0鏉″垎鍊艰緝楂樼殑鏂囩珷瀛樺叆redis  key锛氶閬搃d   value锛?0鏉″垎鍊艰緝楂樼殑鏂囩珷
                    sortAndCache(hotArticleVos, ArticleConstants.HOT_ARTICLE_FIRST_PAGE + wmChannel.getId());
                }
            }
        }


        //璁剧疆鎺ㄨ崘鏁版嵁
        //缁欐枃绔犺繘琛屾帓搴忥紝鍙?0鏉″垎鍊艰緝楂樼殑鏂囩珷瀛樺叆redis  key锛氶閬搃d   value锛?0鏉″垎鍊艰緝楂樼殑鏂囩珷
        sortAndCache(hotArticleVoList, ArticleConstants.HOT_ARTICLE_FIRST_PAGE+ArticleConstants.DEFAULT_TAG);


    }

    /**
     * 鎺掑簭骞朵笖缂撳瓨鏁版嵁
     * @param hotArticleVos
     * @param key
     */
    private void sortAndCache(List<HotArticleVo> hotArticleVos, String key) {
        hotArticleVos = hotArticleVos.stream().sorted(Comparator.comparing(HotArticleVo::getScore).reversed()).collect(Collectors.toList());
        if (hotArticleVos.size() > 30) {
            hotArticleVos = hotArticleVos.subList(0, 30);
        }
        cacheService.set(key, JSON.toJSONString(hotArticleVos));
    }

    /**
     * 璁＄畻鏂囩珷鍒嗗€?
     * @param apArticleList
     * @return
     */
    private List<HotArticleVo> computeHotArticle(List<ApArticle> apArticleList) {

        List<HotArticleVo> hotArticleVoList = new ArrayList<>();

        if(apArticleList != null && apArticleList.size() > 0){
            for (ApArticle apArticle : apArticleList) {
                HotArticleVo hot = new HotArticleVo();
                BeanUtils.copyProperties(apArticle,hot);
                Integer score = computeScore(apArticle);
                hot.setScore(score);
                hotArticleVoList.add(hot);
            }
        }
        return hotArticleVoList;
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

