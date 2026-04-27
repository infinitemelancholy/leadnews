package com.leadnews.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leadnews.model.article.dtos.ArticleHomeDto;
import com.leadnews.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    /**
     * 鍔犺浇鏂囩珷鍒楄〃
     * @param dto
     * @param type  1  鍔犺浇鏇村   2璁拌浇鏈€鏂?
     * @return
     */
    public List<ApArticle> loadArticleList(ArticleHomeDto dto,Short type);

    public List<ApArticle> findArticleListByLast5days(@Param("dayParam") Date dayParam);
}

