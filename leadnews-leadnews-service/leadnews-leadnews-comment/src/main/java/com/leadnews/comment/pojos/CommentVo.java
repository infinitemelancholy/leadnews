package com.leadnews.comment.pojos;

import lombok.Data;

@Data
public class CommentVo extends ApComment {

    /**
     * 0锛氱偣璧?
     * 1锛氬彇娑堢偣璧?
     */
    private Short operation;
}

