package com.leadnews.article.service;

import com.leadnews.model.article.pojos.ApArticle;

public interface ArticleFreemarkerService {

    /**
     * 鐢熸垚闈欐€佹枃浠朵笂浼犲埌minIO涓?
     * @param apArticle
     * @param content
     */
    public void buildArticleToMinIO(ApArticle apArticle,String content);
}

