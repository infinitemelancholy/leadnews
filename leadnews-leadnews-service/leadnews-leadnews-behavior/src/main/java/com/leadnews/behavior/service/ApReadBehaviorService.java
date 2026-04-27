package com.leadnews.behavior.service;

import com.leadnews.model.behavior.dtos.ReadBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;

public interface ApReadBehaviorService {

    /**
     * 淇濆瓨闃呰琛屼负
     * @param dto
     * @return
     */
    public ResponseResult readBehavior(ReadBehaviorDto dto);
}

