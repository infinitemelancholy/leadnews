package com.leadnews.model.user.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * APP鐢ㄦ埛淇℃伅琛?
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("ap_user")
public class ApUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 瀵嗙爜銆侀€氫俊绛夊姞瀵嗙洂
     */
    @TableField("salt")
    private String salt;

    /**
     * 鐢ㄦ埛鍚?
     */
    @TableField("name")
    private String name;

    /**
     * 瀵嗙爜,md5鍔犲瘑
     */
    @TableField("password")
    private String password;

    /**
     * 鎵嬫満鍙?
     */
    @TableField("phone")
    private String phone;

    /**
     * 澶村儚
     */
    @TableField("image")
    private String image;

    /**
     * 0 鐢?
            1 濂?
            2 鏈煡
     */
    @TableField("sex")
    private Boolean sex;

    /**
     * 0 鏈?
            1 鏄?
     */
    @TableField("is_certification")
    private Boolean certification;

    /**
     * 鏄惁韬唤璁よ瘉
     */
    @TableField("is_identity_authentication")
    private Boolean identityAuthentication;

    /**
     * 0姝ｅ父
            1閿佸畾
     */
    @TableField("status")
    private Boolean status;

    /**
     * 0 鏅€氱敤鎴?
            1 鑷獟浣撲汉
            2 澶
     */
    @TableField("flag")
    private Short flag;

    /**
     * 娉ㄥ唽鏃堕棿
     */
    @TableField("created_time")
    private Date createdTime;

}
