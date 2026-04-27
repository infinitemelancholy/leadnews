package com.leadnews.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 鑷獟浣撳浘鏂囧唴瀹逛俊鎭〃
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("wm_news")
public class WmNews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 鑷獟浣撶敤鎴稩D
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 鏍囬
     */
    @TableField("title")
    private String title;

    /**
     * 鍥炬枃鍐呭
     */
    @TableField("content")
    private String content;

    /**
     * 鏂囩珷甯冨眬
            0 鏃犲浘鏂囩珷
            1 鍗曞浘鏂囩珷
            3 澶氬浘鏂囩珷
     */
    @TableField("type")
    private Short type;

    /**
     * 鍥炬枃棰戦亾ID
     */
    @TableField("channel_id")
    private Integer channelId;

    @TableField("labels")
    private String labels;

    /**
     * 鍒涘缓鏃堕棿
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 鎻愪氦鏃堕棿
     */
    @TableField("submited_time")
    private Date submitedTime;

    /**
     * 褰撳墠鐘舵€?
            0 鑽夌
            1 鎻愪氦锛堝緟瀹℃牳锛?
            2 瀹℃牳澶辫触
            3 浜哄伐瀹℃牳
            4 浜哄伐瀹℃牳閫氳繃
            8 瀹℃牳閫氳繃锛堝緟鍙戝竷锛?
            9 宸插彂甯?
     */
    @TableField("status")
    private Short status;

    /**
     * 瀹氭椂鍙戝竷鏃堕棿锛屼笉瀹氭椂鍒欎负绌?
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 鎷掔粷鐞嗙敱
     */
    @TableField("reason")
    private String reason;

    /**
     * 鍙戝竷搴撴枃绔營D
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * //鍥剧墖鐢ㄩ€楀彿鍒嗛殧
     */
    @TableField("images")
    private String images;

    @TableField("enable")
    private Short enable;
    
     //鐘舵€佹灇涓剧被
    @Alias("WmNewsStatus")
    public enum Status{
        NORMAL((short)0),SUBMIT((short)1),FAIL((short)2),ADMIN_AUTH((short)3),ADMIN_SUCCESS((short)4),SUCCESS((short)8),PUBLISHED((short)9);
        short code;
        Status(short code){
            this.code = code;
        }
        public short getCode(){
            return this.code;
        }
    }

}
