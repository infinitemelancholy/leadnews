package com.leadnews.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.article.dtos.ArticleDto;
import com.leadnews.model.article.dtos.ArticleHomeDto;
import com.leadnews.model.article.dtos.ArticleInfoDto;
import com.leadnews.model.article.pojos.ApArticle;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.mess.ArticleVisitStreamMess;

public interface ApArticleService extends IService<ApArticle> {

    /**
     * 鍔犺浇鏂囩珷鍒楄〃
     * @param dto
     * @param type  1 鍔犺浇鏇村   2 鍔犺浇鏈€鏂?
     * @return
     */
    public ResponseResult load(ArticleHomeDto dto,Short type);

    /**
     * 鍔犺浇鏂囩珷鍒楄〃
     * @param dto
     * @param type  1 鍔犺浇鏇村   2 鍔犺浇鏈€鏂?
     * @param firstPage  true  鏄椤? flase 闈為椤?
     * @return
     */
    public ResponseResult load2(ArticleHomeDto dto,Short type,boolean firstPage);

    /**
     * 淇濆瓨app绔浉鍏虫枃绔?
     * @param dto
     * @return
     */
    public ResponseResult saveArticle(ArticleDto dto);

    /**
     * 鍔犺浇鏂囩珷璇︽儏 鏁版嵁鍥炴樉
     * @param dto
     * @return
     */
    public ResponseResult loadArticleBehavior(ArticleInfoDto dto);

    /**
     * 鏇存柊鏂囩珷鐨勫垎鍊? 鍚屾椂鏇存柊缂撳瓨涓殑鐑偣鏂囩珷鏁版嵁
     * @param mess
     */
    public void updateScore(ArticleVisitStreamMess mess);

}

