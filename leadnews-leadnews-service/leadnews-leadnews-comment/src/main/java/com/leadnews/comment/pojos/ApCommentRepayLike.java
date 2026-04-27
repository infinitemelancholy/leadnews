package com.leadnews.comment.pojos;


import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * APP璇勮鍥炲淇℃伅鐐硅禐淇℃伅
 */
@Data
@Document("ap_comment_repay_like")
public class ApCommentRepayLike {

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
    private String commentRepayId;

    /**
     * 0锛氱偣璧?
     * 1锛氬彇娑堢偣璧?
     */
    private Short operation;
}
