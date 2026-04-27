package com.leadnews.apis.article.fallback;

import com.leadnews.apis.article.IArticleClient;
import com.leadnews.model.article.dtos.ArticleDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

@Component
public class IArticleClientFallback implements IArticleClient {
    @Override
    public ResponseResult saveArticle(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"鑾峰彇鏁版嵁澶辫触");
    }
}

