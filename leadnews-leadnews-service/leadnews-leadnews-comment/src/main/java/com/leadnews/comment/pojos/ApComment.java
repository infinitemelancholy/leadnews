package com.leadnews.comment.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * APP璇勮淇℃伅
 */
@Data
@Document("ap_comment")
public class ApComment {

    /**
     * id
     */
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
     * 鏂囩珷id鎴栧姩鎬乮d
     */
    private Long entryId;

    /**
     * 棰戦亾ID
     */
    private Integer channelId;

    /**
     * 璇勮鍐呭绫诲瀷
     * 0 鏂囩珷
     * 1 鍔ㄦ€?
     */
    private Short type;

    /**
     * 璇勮鍐呭
     */
    private String content;

    /**
     * 浣滆€呭ご鍍?
     */
    private String image;

    /**
     * 鐐硅禐鏁?
     */
    private Integer likes;

    /**
     * 鍥炲鏁?
     */
    private Integer reply;

    /**
     * 鏂囩珷鏍囪
     * 0 鏅€氳瘎璁?
     * 1 鐑偣璇勮
     * 2 鎺ㄨ崘璇勮
     * 3 缃《璇勮
     * 4 绮惧搧璇勮
     * 5 澶 璇勮
     */
    private Short flag;

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
     * 璇勮鎺掑垪搴忓彿
     */
    private Integer ord;

    /**
     * 鍒涘缓鏃堕棿
     */
    private Date createdTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private Date updatedTime;

}
