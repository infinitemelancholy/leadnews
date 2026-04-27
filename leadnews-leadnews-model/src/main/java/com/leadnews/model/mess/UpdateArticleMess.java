package com.leadnews.model.mess;

import lombok.Data;

@Data
public class UpdateArticleMess {

    /**
     * 淇敼鏂囩珷鐨勫瓧娈电被鍨?
      */
    private UpdateArticleType type;
    /**
     * 鏂囩珷ID
     */
    private Long articleId;
    /**
     * 淇敼鏁版嵁鐨勫閲忥紝鍙负姝ｈ礋
     */
    private Integer add;

    public enum UpdateArticleType{
        COLLECTION,COMMENT,LIKES,VIEWS;
    }
}
