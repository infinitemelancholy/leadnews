package com.leadnews.model.article.dtos;

import com.leadnews.model.common.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ArticleInfoDto {
    // 璁惧ID
    @IdEncrypt
    Integer equipmentId;
    // 鏂囩珷ID
    @IdEncrypt
    Long articleId;
    // 浣滆€匢D
    @IdEncrypt
    Integer authorId;
}
