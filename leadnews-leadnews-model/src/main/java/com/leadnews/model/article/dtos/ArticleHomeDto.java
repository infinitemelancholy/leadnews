package com.leadnews.model.article.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    // 鏈€澶ф椂闂?
    Date maxBehotTime;
    // 鏈€灏忔椂闂?
    Date minBehotTime;
    // 鍒嗛〉size
    Integer size;
    // 棰戦亾ID
    String tag;
}
