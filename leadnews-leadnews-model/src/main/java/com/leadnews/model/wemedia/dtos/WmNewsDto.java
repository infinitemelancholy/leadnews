package com.leadnews.model.wemedia.dtos;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WmNewsDto {
    
    private Integer id;
     /**
     * 鏍囬
     */
    private String title;
     /**
     * 棰戦亾id
     */
    private Integer channelId;
     /**
     * 鏍囩
     */
    private String labels;
     /**
     * 鍙戝竷鏃堕棿
     */
    private Date publishTime;
     /**
     * 鏂囩珷鍐呭
     */
    private String content;
     /**
     * 鏂囩珷灏侀潰绫诲瀷  0 鏃犲浘 1 鍗曞浘 3 澶氬浘 -1 鑷姩
     */
    private Short type;
     /**
     * 鎻愪氦鏃堕棿
     */
    private Date submitedTime; 
     /**
     * 鐘舵€?鎻愪氦涓?  鑽夌涓?
     */
    private Short status;
     
     /**
     * 灏侀潰鍥剧墖鍒楄〃 澶氬紶鍥句互閫楀彿闅斿紑
     */
    private List<String> images;

    /**
     * 涓婁笅鏋?0 涓嬫灦  1 涓婃灦
     */
    private Short enable;
}
