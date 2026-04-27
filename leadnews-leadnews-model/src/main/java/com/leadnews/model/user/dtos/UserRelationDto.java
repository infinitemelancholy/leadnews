package com.leadnews.model.user.dtos;

import com.leadnews.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UserRelationDto {

    // 鏂囩珷浣滆€匢D
    @IdEncrypt
    Integer authorId;

    // 鏂囩珷id
    @IdEncrypt
    Long articleId;
    /**
     * 鎿嶄綔鏂瑰紡
     * 0  鍏虫敞
     * 1  鍙栨秷
     */
    Short operation;
}
