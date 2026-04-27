package com.leadnews.model.behavior.dtos;

import lombok.Data;

@Data
public class ReadBehaviorDto {

    // 鏂囩珷銆佸姩鎬併€佽瘎璁虹瓑ID
    Long articleId;

    /**
     * 闃呰娆℃暟
     */
    Short count;

    /**
     * 闃呰鏃堕暱锛圫)
     */
    Integer readDuration;

    /**
     * 闃呰鐧惧垎姣?
     */
    Short percentage;

    /**
     * 鍔犺浇鏃堕棿
     */
    Short loadDuration;

}
