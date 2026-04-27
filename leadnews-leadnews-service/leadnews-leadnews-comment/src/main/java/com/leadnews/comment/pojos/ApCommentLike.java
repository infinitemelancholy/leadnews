package com.leadnews.comment.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * APP璇勮淇℃伅鐐硅禐
 */
@Data
@Document("ap_comment_like")
public class ApCommentLike {

    /**
     * id
     */
    private String id;

    /**
     * 鐢ㄦ埛ID
     */
    private Integer authorId;

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
