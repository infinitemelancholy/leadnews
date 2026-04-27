package com.leadnews.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leadnews.article.mapper.ApArticleContentMapper;
import com.leadnews.article.service.ApArticleService;
import com.leadnews.article.service.ArticleFreemarkerService;
import com.leadnews.common.constants.ArticleConstants;
import com.leadnews.file.service.FileStorageService;
import com.leadnews.model.article.pojos.ApArticle;
import com.leadnews.model.search.vos.SearchArticleVo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class ArticleFreemarkerServiceImpl implements ArticleFreemarkerService {

    @Autowired
    private ApArticleContentMapper apArticleContentMapper;

    @Autowired
    private Configuration configuration;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    @Lazy
    private ApArticleService apArticleService;

    /**
     * 鐢熸垚闈欐€佹枃浠朵笂浼犲埌minIO涓?
     * @param apArticle
     * @param content
     */
    @Async
    @Override
    public void buildArticleToMinIO(ApArticle apArticle, String content) {
        //宸茬煡鏂囩珷鐨刬d
        //4.1 鑾峰彇鏂囩珷鍐呭
        if(StringUtils.isNotBlank(content)){
            //4.2 鏂囩珷鍐呭閫氳繃freemarker鐢熸垚html鏂囦欢
            Template template = null;
            StringWriter out = new StringWriter();
            try {
                template = configuration.getTemplate("article.ftl");
                //鏁版嵁妯″瀷
                Map<String,Object> contentDataModel = new HashMap<>();
                contentDataModel.put("content", JSONArray.parseArray(content));
                //鍚堟垚
                template.process(contentDataModel,out);
            } catch (Exception e) {
                e.printStackTrace();
            }


            //4.3 鎶奾tml鏂囦欢涓婁紶鍒癿inio涓?
            InputStream in = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticle.getId() + ".html", in);


            //4.4 淇敼ap_article琛紝淇濆瓨static_url瀛楁
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId,apArticle.getId())
                    .set(ApArticle::getStaticUrl,path));

            //鍙戦€佹秷鎭紝鍒涘缓绱㈠紩
            createArticleESIndex(apArticle,content,path);

        }
    }

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 閫佹秷鎭紝鍒涘缓绱㈠紩
     * @param apArticle
     * @param content
     * @param path
     */
    private void createArticleESIndex(ApArticle apArticle, String content, String path) {
        SearchArticleVo vo = new SearchArticleVo();
        BeanUtils.copyProperties(apArticle,vo);
        vo.setContent(content);
        vo.setStaticUrl(path);

        kafkaTemplate.send(ArticleConstants.ARTICLE_ES_SYNC_TOPIC, JSON.toJSONString(vo));
    }

}

