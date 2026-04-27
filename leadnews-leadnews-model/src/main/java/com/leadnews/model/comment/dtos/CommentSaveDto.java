package com.leadnews.model.comment.dtos;

import com.leadnews.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class CommentSaveDto {

    /**
     * 鏂囩珷id
     */
    @IdEncrypt
    private Long articleId;

    /**
     * 璇勮鍐呭
     */
    private String content;
}
