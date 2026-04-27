package com.leadnews.model.behavior.dtos;

import lombok.Data;

@Data
public class LikesBehaviorDto {


    // 鏂囩珷銆佸姩鎬併€佽瘎璁虹瓑ID
    Long articleId;
    /**
     * 鍠滄鍐呭绫诲瀷
     * 0鏂囩珷
     * 1鍔ㄦ€?
     * 2璇勮
     */
    Short type;

    /**
     * 鍠滄鎿嶄綔鏂瑰紡
     * 0 鐐硅禐
     * 1 鍙栨秷鐐硅禐
     */
    Short operation;
}

