package com.leadnews.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 鑷獟浣撳浘鏂囩礌鏉愪俊鎭〃
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("wm_material")
public class WmMaterial implements Serializable {

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
     * 鍥剧墖鍦板潃
     */
    @TableField("url")
    private String url;

    /**
     * 绱犳潗绫诲瀷
            0 鍥剧墖
            1 瑙嗛
     */
    @TableField("type")
    private Short type;

    /**
     * 鏄惁鏀惰棌
     */
    @TableField("is_collection")
    private Short isCollection;

    /**
     * 鍒涘缓鏃堕棿
     */
    @TableField("created_time")
    private Date createdTime;

}
