package com.leadnews.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 鏂囩珷淇℃伅琛紝瀛樺偍宸插彂甯冪殑鏂囩珷
 * </p>
 *
 * @author leadnews
 */

@Data
@TableName("ap_article")
public class ApArticle implements Serializable {

    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;


    /**
     * 鏍囬
     */
    private String title;

    /**
     * 浣滆€卛d
     */
    @TableField("author_id")
    private Long authorId;

    /**
     * 浣滆€呭悕绉?
     */
    @TableField("author_name")
    private String authorName;

    /**
     * 棰戦亾id
     */
    @TableField("channel_id")
    private Integer channelId;

    /**
     * 棰戦亾鍚嶇О
     */
    @TableField("channel_name")
    private String channelName;

    /**
     * 鏂囩珷甯冨眬  0 鏃犲浘鏂囩珷   1 鍗曞浘鏂囩珷    2 澶氬浘鏂囩珷
     */
    private Short layout;

    /**
     * 鏂囩珷鏍囪  0 鏅€氭枃绔?  1 鐑偣鏂囩珷   2 缃《鏂囩珷   3 绮惧搧鏂囩珷   4 澶 鏂囩珷
     */
    private Byte flag;

    /**
     * 鏂囩珷灏侀潰鍥剧墖 澶氬紶閫楀彿鍒嗛殧
     */
    private String images;

    /**
     * 鏍囩
     */
    private String labels;

    /**
     * 鐐硅禐鏁伴噺
     */
    private Integer likes;

    /**
     * 鏀惰棌鏁伴噺
     */
    private Integer collection;

    /**
     * 璇勮鏁伴噺
     */
    private Integer comment;

    /**
     * 闃呰鏁伴噺
     */
    private Integer views;

    /**
     * 鐪佸競
     */
    @TableField("province_id")
    private Integer provinceId;

    /**
     * 甯傚尯
     */
    @TableField("city_id")
    private Integer cityId;

    /**
     * 鍖哄幙
     */
    @TableField("county_id")
    private Integer countyId;

    /**
     * 鍒涘缓鏃堕棿
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 鍙戝竷鏃堕棿
     */
    @TableField("publish_time")
    private Date publishTime;

    /**
     * 鍚屾鐘舵€?
     */
    @TableField("sync_status")
    private Boolean syncStatus;

    /**
     * 鏉ユ簮
     */
    private Boolean origin;

    /**
     * 闈欐€侀〉闈㈠湴鍧€
     */
    @TableField("static_url")
    private String staticUrl;
}

