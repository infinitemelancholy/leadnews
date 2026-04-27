package com.leadnews.model.behavior.dtos;

import com.leadnews.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UnLikesBehaviorDto {
    // 鏂囩珷ID
    @IdEncrypt
    Long articleId;

    /**
     * 涓嶅枩娆㈡搷浣滄柟寮?
     * 0 涓嶅枩娆?
     * 1 鍙栨秷涓嶅枩娆?
     */
    Short type;

}
