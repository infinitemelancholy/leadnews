package com.leadnews.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {

    /**
     * 鏌ヨ鎵€鏈夐閬?
     * @return
     */
    public ResponseResult findAll();


}
