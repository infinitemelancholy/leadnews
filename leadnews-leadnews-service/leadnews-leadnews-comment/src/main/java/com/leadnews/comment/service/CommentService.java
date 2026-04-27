package com.leadnews.comment.service;

import com.leadnews.model.comment.dtos.CommentDto;
import com.leadnews.model.comment.dtos.CommentLikeDto;
import com.leadnews.model.comment.dtos.CommentSaveDto;
import com.leadnews.model.common.dtos.ResponseResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface CommentService {

    /**
     * 淇濆瓨璇勮
     * @param dto
     * @return
     */
    public ResponseResult saveComment(CommentSaveDto dto);

    /**
     * 鐐硅禐
     * @param dto
     * @return
     */
    public ResponseResult like(CommentLikeDto dto);

    /**
     * 鍔犺浇璇勮鍒楄〃
     * @param dto
     * @return
     */
    public ResponseResult findByArticleId(CommentDto dto);
}

