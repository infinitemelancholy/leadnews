package com.leadnews.behavior.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.behavior.service.ApReadBehaviorService;
import com.leadnews.common.constants.HotArticleConstants;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.behavior.dtos.ReadBehaviorDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.mess.UpdateArticleMess;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ApReadBehaviorServiceImpl implements ApReadBehaviorService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Override
    public ResponseResult readBehavior(ReadBehaviorDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }
        //鏇存柊闃呰娆℃暟
        String readBehaviorJson = (String) cacheService.hGet(dto.getArticleId().toString(), user.getId().toString());
        if(StringUtils.isNotBlank(readBehaviorJson)){
            ReadBehaviorDto readBehaviorDto = JSON.parseObject(readBehaviorJson, ReadBehaviorDto.class);
            dto.setCount((short)(readBehaviorDto.getCount()+dto.getCount()));
        }
        // 淇濆瓨褰撳墠key
        log.info("淇濆瓨褰撳墠key:{} {} {}", dto.getArticleId(), user.getId(), dto);
        cacheService.hPut("READ-BEHAVIOR-" + dto.getArticleId().toString(), user.getId().toString(), JSON.toJSONString(dto));

        //鍙戦€佹秷鎭紝鏁版嵁鑱氬悎
        UpdateArticleMess mess = new UpdateArticleMess();
        mess.setArticleId(dto.getArticleId());
        mess.setType(UpdateArticleMess.UpdateArticleType.VIEWS);
        mess.setAdd(1);
        kafkaTemplate.send(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC,JSON.toJSONString(mess));

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }
}

