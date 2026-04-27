package com.leadnews.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.pojos.WmChannel;
import com.leadnews.wemedia.mapper.WmChannelMapper;
import com.leadnews.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper, WmChannel> implements WmChannelService {


    /**
     * 鏌ヨ鎵€鏈夐閬?
     * @return
     */
    @Override
    public ResponseResult findAll() {
        return ResponseResult.okResult(list());
    }
}
