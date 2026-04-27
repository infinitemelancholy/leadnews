package com.leadnews.article.job;

import com.leadnews.article.service.HotArticleService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ComputeHotArticleJob {

    @Autowired
    private HotArticleService hotArticleService;

    @XxlJob("computeHotArticleJob")
    public void handle(){
        log.info("鐑枃绔犲垎鍊艰绠楄皟搴︿换鍔″紑濮嬫墽琛?..");
        hotArticleService.computeHotArticle();
        log.info("鐑枃绔犲垎鍊艰绠楄皟搴︿换鍔＄粨鏉?..");

    }
}

