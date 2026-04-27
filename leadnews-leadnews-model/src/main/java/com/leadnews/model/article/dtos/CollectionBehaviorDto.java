package com.leadnews.model.article.dtos;

import com.leadnews.model.common.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
public class CollectionBehaviorDto {

    // 鏂囩珷銆佸姩鎬両D
    @IdEncrypt
    Long entryId;
    /**
     * 鏀惰棌鍐呭绫诲瀷
     * 0鏂囩珷
     * 1鍔ㄦ€?
     */
    Short type;

    /**
     * 鎿嶄綔绫诲瀷
     * 0鏀惰棌
     * 1鍙栨秷鏀惰棌
     */
    Short operation;

    Date publishedTime;

}
