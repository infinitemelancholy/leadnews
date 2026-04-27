package com.leadnews.model.wemedia.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 鑷獟浣撳浘鏂囧紩鐢ㄧ礌鏉愪俊鎭〃
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("wm_news_material")
public class WmNewsMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 绱犳潗ID
     */
    @TableField("material_id")
    private Integer materialId;

    /**
     * 鍥炬枃ID
     */
    @TableField("news_id")
    private Integer newsId;

    /**
     * 寮曠敤绫诲瀷
            0 鍐呭寮曠敤
            1 涓诲浘寮曠敤
     */
    @TableField("type")
    private Short type;

    /**
     * 寮曠敤鎺掑簭
     */
    @TableField("ord")
    private Short ord;

}
