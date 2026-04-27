package com.leadnews.model.mess;

import lombok.Data;

@Data
public class ArticleVisitStreamMess {
    /**
     * 鏂囩珷id
     */
    private Long articleId;
    /**
     * 闃呰
     */
    private int view;
    /**
     * 鏀惰棌
     */
    private int collect;
    /**
     * 璇勮
     */
    private int comment;
    /**
     * 鐐硅禐
     */
    private int like;
}
