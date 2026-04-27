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
 * 棰戦亾淇℃伅琛?
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("wm_channel")
public class WmChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 棰戦亾鍚嶇О
     */
    @TableField("name")
    private String name;

    /**
     * 棰戦亾鎻忚堪
     */
    @TableField("description")
    private String description;

    /**
     * 鏄惁榛樿棰戦亾
     * 1锛氶粯璁?    true
     * 0锛氶潪榛樿   false
     */
    @TableField("is_default")
    private Boolean isDefault;

    /**
     * 鏄惁鍚敤
     * 1锛氬惎鐢?  true
     * 0锛氱鐢?  false
     */
    @TableField("status")
    private Boolean status;

    /**
     * 榛樿鎺掑簭
     */
    @TableField("ord")
    private Integer ord;

    /**
     * 鍒涘缓鏃堕棿
     */
    @TableField("created_time")
    private Date createdTime;

}
