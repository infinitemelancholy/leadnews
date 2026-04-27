package com.leadnews.behavior.service;

import com.leadnews.model.behavior.dtos.LikesBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;

public interface ApLikesBehaviorService {

    /**
     * 瀛樺偍鍠滄鏁版嵁
     * @param dto
     * @return
     */
    public ResponseResult like(LikesBehaviorDto dto);
}

