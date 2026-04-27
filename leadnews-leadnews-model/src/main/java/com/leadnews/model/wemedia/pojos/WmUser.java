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
 * 鑷獟浣撶敤鎴蜂俊鎭〃
 * </p>
 *
 * @author leadnews
 */
@Data
@TableName("wm_user")
public class WmUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 涓婚敭
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("ap_user_id")
    private Integer apUserId;

    @TableField("ap_author_id")
    private Integer apAuthorId;

    /**
     * 鐧诲綍鐢ㄦ埛鍚?
     */
    @TableField("name")
    private String name;

    /**
     * 鐧诲綍瀵嗙爜
     */
    @TableField("password")
    private String password;

    /**
     * 鐩?
     */
    @TableField("salt")
    private String salt;

    /**
     * 鏄电О
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 澶村儚
     */
    @TableField("image")
    private String image;

    /**
     * 褰掑睘鍦?
     */
    @TableField("location")
    private String location;

    /**
     * 鎵嬫満鍙?
     */
    @TableField("phone")
    private String phone;

    /**
     * 鐘舵€?
            0 鏆傛椂涓嶅彲鐢?
            1 姘镐箙涓嶅彲鐢?
            9 姝ｅ父鍙敤
     */
    @TableField("status")
    private Integer status;

    /**
     * 閭
     */
    @TableField("email")
    private String email;

    /**
     * 璐﹀彿绫诲瀷
            0 涓汉 
            1 浼佷笟
            2 瀛愯处鍙?
     */
    @TableField("type")
    private Integer type;

    /**
     * 杩愯惀璇勫垎
     */
    @TableField("score")
    private Integer score;

    /**
     * 鏈€鍚庝竴娆＄櫥褰曟椂闂?
     */
    @TableField("login_time")
    private Date loginTime;

    /**
     * 鍒涘缓鏃堕棿
     */
    @TableField("created_time")
    private Date createdTime;

}
