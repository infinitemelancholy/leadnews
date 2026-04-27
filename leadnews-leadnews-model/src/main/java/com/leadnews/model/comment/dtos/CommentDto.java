package com.leadnews.model.comment.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    /**
     * 鏂囩珷id
     */
    private Long articleId;

    // 鏈€灏忔椂闂?
    private Date minDate;

    //鏄惁鏄椤?
    private Short index;

}
