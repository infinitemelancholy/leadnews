package com.leadnews.article.feign;

import com.leadnews.apis.article.IArticleClient;
import com.leadnews.article.service.ApArticleService;
import com.leadnews.model.article.dtos.ArticleDto;
import com.leadnews.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleClient implements IArticleClient {

    @Autowired
    private ApArticleService apArticleService;

    @PostMapping("/api/v1/article/save")
    @Override
    public ResponseResult saveArticle(@RequestBody ArticleDto dto) {
        return apArticleService.saveArticle(dto);
    }
}

