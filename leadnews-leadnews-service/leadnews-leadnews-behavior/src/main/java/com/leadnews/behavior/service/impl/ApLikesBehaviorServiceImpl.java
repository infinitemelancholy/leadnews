package com.leadnews.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.behavior.service.ApLikesBehaviorService;
import com.leadnews.common.constants.HotArticleConstants;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.behavior.dtos.LikesBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.mess.UpdateArticleMess;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class ApLikesBehaviorServiceImpl implements ApLikesBehaviorService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult like(LikesBehaviorDto dto) {

        //1.妫€鏌ュ弬鏁?
        if (dto == null || dto.getArticleId() == null || checkParam(dto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.LIKES);

        //3.鐐硅禐  淇濆瓨鏁版嵁
        if(dto.getOperation() == 0){
            Object obj = cacheService.hGet("LIKE-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString());
            if(obj != null){
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"宸茬偣璧?);
            }
            // 淇濆瓨褰撳墠key
            log.info("淇濆瓨褰撳墠key:{} ,{}, {}", dto.getArticleId(), user.getId(), dto);
            cacheService.hPut("LIKE-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString(), JSON.toJSONString(dto));
            mess.setAdd(1);
        }else {
            // 鍒犻櫎褰撳墠key
            log.info("鍒犻櫎褰撳墠key:{}, {}", dto.getArticleId(), user.getId());
            cacheService.hDelete("LIKE-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString());
            mess.setAdd(-1);
        }

        //鍙戦€佹秷鎭紝鏁版嵁鑱氬悎
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }

    /**
     * 妫€鏌ュ弬鏁?
     * @return
     */
    private boolean checkParam(LikesBehaviorDto dto){
        if(dto.getType() > 2 || dto.getType() < 0 || dto.getOperation() > 1 || dto.getOperation() < 0){
            return true;
        }
        return false;
    }
}

