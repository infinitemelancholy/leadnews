package com.leadnews.model.article.vos;

import com.leadnews.model.article.pojos.ApArticle;
import lombok.Data;

@Data
public class HotArticleVo extends ApArticle {
    /**
     * 鏂囩珷鍒嗗€?
     */
    private Integer score;
}

