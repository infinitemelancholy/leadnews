package com.leadnews.model.article.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * APP宸插彂甯冩枃绔犻厤缃〃
 * </p>
 *
 * @author leadnews
 */

@Data
@NoArgsConstructor
@TableName("ap_article_config")
public class ApArticleConfig implements Serializable {

    public ApArticleConfig(Long articleId){
        this.articleId = articleId;
        this.isDelete = false;
        this.isDown = false;
        this.isForward = true;
        this.isComment = true;
    }



    @TableId(value = "id",type = IdType.ID_WORKER)
    private Long id;

    /**
     * 鏂囩珷id
     */
    @TableField("article_id")
    private Long articleId;

    /**
     * 鏄惁鍙瘎璁?
     * true: 鍙互璇勮   1
     * false: 涓嶅彲璇勮  0
     */
    @TableField("is_comment")
    private Boolean isComment;

    /**
     * 鏄惁杞彂
     * true: 鍙互杞彂   1
     * false: 涓嶅彲杞彂  0
     */
    @TableField("is_forward")
    private Boolean isForward;

    /**
     * 鏄惁涓嬫灦
     * true: 涓嬫灦   1
     * false: 娌℃湁涓嬫灦  0
     */
    @TableField("is_down")
    private Boolean isDown;

    /**
     * 鏄惁宸插垹闄?
     * true: 鍒犻櫎   1
     * false: 娌℃湁鍒犻櫎  0
     */
    @TableField("is_delete")
    private Boolean isDelete;
}

