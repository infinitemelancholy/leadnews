package com.leadnews.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.article.mapper.ApArticleConfigMapper;
import com.leadnews.article.service.ApArticleConfigService;
import com.leadnews.model.article.pojos.ApArticleConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Slf4j
@Transactional
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {
    /**
     * 淇敼鏂囩珷
     * @param map
     */
    @Override
    public void updateByMap(Map map) {
        //0 涓嬫灦  1 涓婃灦
        Object enable = map.get("enable");
        boolean isDown = true;
        if(enable.equals(1)){
           isDown = false;
        }
        //淇敼鏂囩珷
        update(Wrappers.<ApArticleConfig>lambdaUpdate().eq(ApArticleConfig::getArticleId,map.get("articleId"))
                .set(ApArticleConfig::getIsDown,isDown));

    }
}

