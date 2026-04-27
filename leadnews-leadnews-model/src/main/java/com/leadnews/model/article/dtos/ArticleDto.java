package com.leadnews.model.article.dtos;

import com.leadnews.model.article.pojos.ApArticle;
import lombok.Data;

@Data
public class ArticleDto  extends ApArticle {

    /**
     * 鏂囩珷鍐呭
     */
    private String content;
}

