package com.leadnews.article.service.impl;

import com.leadnews.article.ArticleApplication;
import com.leadnews.article.service.HotArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = ArticleApplication.class)
@RunWith(SpringRunner.class)
public class HotArticleServiceImplTest {

    @Autowired
    private HotArticleService hotArticleService;

    @Test
    public void computeHotArticle() {
        hotArticleService.computeHotArticle();
    }
}
