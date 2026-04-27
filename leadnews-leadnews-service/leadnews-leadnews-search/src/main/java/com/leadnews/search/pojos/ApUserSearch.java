package com.leadnews.search.pojos;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP鐢ㄦ埛鎼滅储淇℃伅琛?
 * </p>
 * @author leadnews
 */
@Data
@Document("ap_user_search")
public class ApUserSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭
     */
    private String id;

    /**
     * 鐢ㄦ埛ID
     */
    private Integer userId;

    /**
     * 鎼滅储璇?
     */
    private String keyword;

    /**
     * 鍒涘缓鏃堕棿
     */
    private Date createdTime;

}
