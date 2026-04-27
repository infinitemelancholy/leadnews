package com.leadnews.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.behavior.service.ApUnlikesBehaviorService;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.behavior.dtos.UnLikesBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * APP涓嶅枩娆㈣涓鸿〃 鏈嶅姟瀹炵幇绫?
 * </p>
 *
 * @author leadnews
 */
@Slf4j
@Service
public class ApUnlikesBehaviorServiceImpl implements ApUnlikesBehaviorService {

    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseResult unLike(UnLikesBehaviorDto dto) {

        if(dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        if(dto.getType()==0){
            log.info("淇濆瓨褰撳墠key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hPut("UNLIKE-BEHAVIOR-"+dto.getArticleId().toString(),user.getId().toString(), JSON.toJSONString(dto));
        }else {
            log.info("鍒犻櫎褰撳墠key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hDelete("UNLIKE-BEHAVIOR-"+dto.getArticleId().toString(),user.getId().toString());
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}
