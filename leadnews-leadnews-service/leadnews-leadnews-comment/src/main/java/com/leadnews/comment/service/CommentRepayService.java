package com.leadnews.comment.service;

import com.leadnews.model.comment.dtos.CommentRepayDto;
import com.leadnews.model.comment.dtos.CommentRepayLikeDto;
import com.leadnews.model.comment.dtos.CommentRepaySaveDto;
import com.leadnews.model.common.dtos.ResponseResult;

/**
 * 璇勮鍥炲
 */
public interface CommentRepayService {

    /**
     * 鏌ョ湅鏇村鍥炲鍐呭
     * @param dto
     * @return
     */
    public ResponseResult loadCommentRepay(CommentRepayDto dto);

    /**
     * 淇濆瓨鍥炲
     * @return
     */
    public ResponseResult saveCommentRepay(CommentRepaySaveDto dto);

    /**
     * 鐐硅禐鍥炲鐨勮瘎璁?
     * @param dto
     * @return
     */
    public ResponseResult saveCommentRepayLike(CommentRepayLikeDto dto);
}
