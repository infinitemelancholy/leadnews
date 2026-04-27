package com.leadnews.article.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.article.service.ApCollectionService;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.article.dtos.CollectionBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApCollectionServiceImpl implements ApCollectionService {

    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult collection(CollectionBehaviorDto dto) {
        //鏉′欢鍒ゆ柇
        if(dto == null || dto.getEntryId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //鍒ゆ柇鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //鏌ヨ
        String collectionJson = (String) cacheService.hGet("COLLECTION-BEHAVIOR-" + dto.getEntryId(), user.getId().toString());
        if(StringUtils.isNotBlank(collectionJson) && dto.getOperation() == 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"宸叉敹钘?);
        }

        //鏀惰棌
        if(dto.getOperation() == 0){
            log.info("鏂囩珷鏀惰棌锛屼繚瀛榢ey:{},{},{}",dto.getEntryId(),user.getId().toString(), JSON.toJSONString(dto));
            cacheService.hPut("COLLECTION-BEHAVIOR-"+dto.getEntryId(),user.getId().toString(), JSON.toJSONString(dto));
        }else {
            //鍙栨秷鏀惰棌
            log.info("鏂囩珷鏀惰棌锛屽垹闄ey:{},{},{}",dto.getEntryId(),user.getId().toString(), JSON.toJSONString(dto));
            cacheService.hDelete("COLLECTION-BEHAVIOR-"+dto.getEntryId(),user.getId().toString());
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
