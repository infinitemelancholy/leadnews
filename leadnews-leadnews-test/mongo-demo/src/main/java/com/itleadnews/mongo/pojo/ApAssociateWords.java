package com.leadnews.mongo.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 鑱旀兂璇嶈〃
 * </p>
 *
 * @author leadnews
 */
@Data
@Document("ap_associate_words")
public class ApAssociateWords implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 鑱旀兂璇?     */
    private String associateWords;

    /**
     * 鍒涘缓鏃堕棿
     */
    private Date createdTime;

}
