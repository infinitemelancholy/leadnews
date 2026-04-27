package com.leadnews.model.comment.dtos;

import lombok.Data;

@Data
public class CommentLikeDto {

    /**
     * 璇勮id
     */
    private String commentId;

    /**
     * 0锛氱偣璧?
     * 1锛氬彇娑堢偣璧?
     */
    private Short operation;
}
