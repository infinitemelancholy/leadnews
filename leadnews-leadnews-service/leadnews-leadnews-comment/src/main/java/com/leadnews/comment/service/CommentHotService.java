package com.leadnews.comment.service;


import com.leadnews.comment.pojos.ApComment;

public interface CommentHotService {

    /**
     * 璁＄畻鐑偣璇勮
     * @param entryId  鏂囩珷id
     * @param apComment 褰撳墠璇勮瀵硅薄
     */
    public void findHotComment(Long entryId, ApComment apComment);
}

