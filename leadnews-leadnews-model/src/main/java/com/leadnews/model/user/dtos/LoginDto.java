package com.leadnews.model.user.dtos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDto {

    /**
     * 鎵嬫満鍙?
     */
    @ApiModelProperty(value = "鎵嬫満鍙?,required = true)
    private String phone;

    /**
     * 瀵嗙爜
     */
    @ApiModelProperty(value = "瀵嗙爜",required = true)
    private String password;
}

