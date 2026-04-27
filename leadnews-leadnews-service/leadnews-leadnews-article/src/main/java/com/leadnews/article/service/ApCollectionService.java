package com.leadnews.article.service;

import com.leadnews.model.article.dtos.CollectionBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;

public interface ApCollectionService {

    /**
     * 鏀惰棌
     * @param dto
     * @return
     */
    public ResponseResult collection(CollectionBehaviorDto dto);
}

