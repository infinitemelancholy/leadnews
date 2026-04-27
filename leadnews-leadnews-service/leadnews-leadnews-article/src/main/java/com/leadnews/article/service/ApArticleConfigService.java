package com.leadnews.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.article.pojos.ApArticleConfig;

import java.util.Map;

public interface ApArticleConfigService extends IService<ApArticleConfig> {
    /**
     * 淇敼鏂囩珷
     * @param map
     */
    void updateByMap(Map map);
}

