package com.leadnews.behavior.service;

import com.leadnews.model.behavior.dtos.UnLikesBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;

/**
 * <p>
 * APP涓嶅枩娆㈣涓鸿〃 鏈嶅姟绫?
 * </p>
 *
 * @author leadnews
 */
public interface ApUnlikesBehaviorService {

    /**
     * 涓嶅枩娆?
     * @param dto
     * @return
     */
    public ResponseResult unLike(UnLikesBehaviorDto dto);

}
