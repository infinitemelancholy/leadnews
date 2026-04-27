package com.leadnews.user.service;


import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.user.dtos.UserRelationDto;



public interface ApUserRelationService {
    /**
     * 鐢ㄦ埛鍏虫敞/鍙栨秷鍏虫敞
     * @param dto
     * @return
     */
    public ResponseResult follow(UserRelationDto dto);
}
