package com.leadnews.user.service.impl;

import com.leadnews.common.redis.CacheService;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.user.dtos.UserRelationDto;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.user.service.ApUserRelationService;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ApUserRelationServiceImpl implements ApUserRelationService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;


    /**
     * 鐢ㄦ埛鍏虫敞/鍙栨秷鍏虫敞
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult follow(UserRelationDto dto) {
        //1 鍙傛暟鏍￠獙
        if (dto.getOperation() == null ||
                dto.getOperation() < 0 || dto.getOperation() > 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2 鍒ゆ柇鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if (user == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        Integer apUserId = user.getId();

        //3 鍏虫敞 apuser:follow:  apuser:fans:
        Integer followUserId = dto.getAuthorId();
        if (dto.getOperation() == 0) {
            // 灏嗗鏂瑰啓鍏ユ垜鐨勫叧娉ㄤ腑
            cacheService.zAdd("apuser:follow:" + apUserId, followUserId.toString(), System.currentTimeMillis());
            // 灏嗘垜鍐欏叆瀵规柟鐨勭矇涓濅腑
            cacheService.zAdd("apuser:fans:" + followUserId, apUserId.toString(), System.currentTimeMillis());

        } else {
            // 鍙栨秷鍏虫敞
            cacheService.zRemove("apuser:follow:" + apUserId, followUserId.toString());
            cacheService.zRemove("apuser:fans:" + followUserId, apUserId.toString());
        }

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }
}
