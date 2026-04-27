package com.leadnews.model.behavior.dtos;

import lombok.Data;

@Data
public class FollowBehaviorDto {
    //鏂囩珷id
    Long articleId;
    //鍏虫敞鐨刬d
    Integer followId;
    //鐢ㄦ埛id
    Integer userId;
}
