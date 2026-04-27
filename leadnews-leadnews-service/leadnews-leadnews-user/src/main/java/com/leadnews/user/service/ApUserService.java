package com.leadnews.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.user.dtos.LoginDto;
import com.leadnews.model.user.pojos.ApUser;

public interface ApUserService extends IService<ApUser> {
    /**
     * app绔櫥褰曞姛鑳?
     * @param dto
     * @return
     */
    public ResponseResult login(LoginDto dto);
}

