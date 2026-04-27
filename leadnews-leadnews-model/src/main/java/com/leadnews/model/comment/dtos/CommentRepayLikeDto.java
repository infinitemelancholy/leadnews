package com.leadnews.model.comment.dtos;

import lombok.Data;

@Data
public class CommentRepayLikeDto {

    /**
     * 鍥炲id
     */
    private String commentRepayId;

    /**
     * 0锛氱偣璧?
     * 1锛氬彇娑堢偣璧?
     */
    private Short operation;
}
