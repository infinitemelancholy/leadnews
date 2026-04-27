package com.leadnews.es.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leadnews.es.pojo.SearchArticleVo;
import com.leadnews.model.article.pojos.ApArticle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    public List<SearchArticleVo> loadArticleList();

}

