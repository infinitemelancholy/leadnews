package com.leadnews.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.dtos.WmLoginDto;
import com.leadnews.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {

    /**
     * 鑷獟浣撶鐧诲綍
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}
