package com.leadnews.model.wemedia.dtos;

import com.leadnews.model.common.dtos.PageRequestDto;
import lombok.Data;

import java.util.Date;

@Data
public class WmNewsPageReqDto extends PageRequestDto {

    /**
     * 鐘舵€?
     */
    private Short status;
    /**
     * 寮€濮嬫椂闂?
     */
    private Date beginPubDate;
    /**
     * 缁撴潫鏃堕棿
     */
    private Date endPubDate;
    /**
     * 鎵€灞為閬揑D
     */
    private Integer channelId;
    /**
     * 鍏抽敭瀛?
     */
    private String keyword;
}
