package com.leadnews.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.wemedia.dtos.WmNewsDto;
import com.leadnews.model.wemedia.dtos.WmNewsPageReqDto;
import com.leadnews.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {

    /**
     * 鏉′欢鏌ヨ鏂囩珷鍒楄〃
     * @param dto
     * @return
     */
    public ResponseResult findList(WmNewsPageReqDto dto);

    /**
     * 鍙戝竷淇敼鏂囩珷鎴栦繚瀛樹负鑽夌
     * @param dto
     * @return
     */
    public ResponseResult submitNews(WmNewsDto dto);

    /**
     * 鏂囩珷鐨勪笂涓嬫灦
     * @param dto
     * @return
     */
    public ResponseResult downOrUp(WmNewsDto dto);


}

