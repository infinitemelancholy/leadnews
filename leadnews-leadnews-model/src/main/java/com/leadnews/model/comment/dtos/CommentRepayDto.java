package com.leadnews.model.comment.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CommentRepayDto {

    /**
     * 璇勮id
     */
    private String commentId;

    private Integer size;

    // 鏈€灏忔椂闂?
    private Date minDate;
}
