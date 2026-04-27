package com.leadnews.comment.pojos;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * APP璇勮鍥炲淇℃伅
 */
@Data
@Document("ap_comment_repay")
public class ApCommentRepay {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 鐢ㄦ埛ID
     */
    private Integer authorId;

    /**
     * 鐢ㄦ埛鏄电О
     */
    private String authorName;

    /**
     * 璇勮id
     */
    private String commentId;

    /**
     * 鍥炲鍐呭
     */
    private String content;

    /**
     * 鐐硅禐鏁?
     */
    private Integer likes;

    /**
     * 缁忓害
     */
    private BigDecimal longitude;

    /**
     * 缁村害
     */
    private BigDecimal latitude;

    /**
     * 鍦扮悊浣嶇疆
     */
    private String address;

    /**
     * 鍒涘缓鏃堕棿
     */
    private Date createdTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private Date updatedTime;

}
