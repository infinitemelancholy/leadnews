package com.leadnews.comment.service.impl;

import com.leadnews.apis.user.IUserClient;
import com.leadnews.comment.pojos.ApComment;
import com.leadnews.comment.pojos.ApCommentRepay;
import com.leadnews.comment.pojos.ApCommentRepayLike;
import com.leadnews.comment.pojos.CommentRepayVo;
import com.leadnews.comment.service.CommentRepayService;
import com.leadnews.model.comment.dtos.CommentRepayDto;
import com.leadnews.model.comment.dtos.CommentRepayLikeDto;
import com.leadnews.model.comment.dtos.CommentRepaySaveDto;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentRepayServiceImpl implements CommentRepayService {

    @Autowired
    private IUserClient userClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult loadCommentRepay(CommentRepayDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || dto.getCommentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int size = 20;

        //2.鍔犺浇鏁版嵁
        Query query = Query.query(Criteria.where("commentId").is(dto.getCommentId()).and("createdTime").lt(dto.getMinDate()));
        query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
        List<ApCommentRepay> list = mongoTemplate.find(query, ApCommentRepay.class);

        //3.鏁版嵁灏佽杩斿洖
        //3.1 鐢ㄦ埛鏈櫥褰?
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.okResult(list);
        }

        //3.2 鐢ㄦ埛宸茬櫥褰?

        //闇€瑕佹煡璇㈠綋鍓嶈瘎璁轰腑鍝簺鏁版嵁琚偣璧炰簡
        List<String> idList = list.stream().map(x -> x.getId()).collect(Collectors.toList());
        Query query1 = Query.query(Criteria.where("commentRepayId").in(idList).and("authorId").is(user.getId()));
        List<ApCommentRepayLike> apCommentRepayLikes = mongoTemplate.find(query1, ApCommentRepayLike.class);
        if(apCommentRepayLikes == null || apCommentRepayLikes.size() == 0 ){
            return ResponseResult.okResult(list);
        }

        List<CommentRepayVo> resultList = new ArrayList<>();
        list.forEach(x->{
            CommentRepayVo vo = new CommentRepayVo();
            BeanUtils.copyProperties(x,vo);
            for (ApCommentRepayLike apCommentRepayLike : apCommentRepayLikes) {
                if(x.getId().equals(apCommentRepayLike.getCommentRepayId())){
                    vo.setOperation((short)0);
                    break;
                }
            }
            resultList.add(vo);
        });

        return ResponseResult.okResult(resultList);
    }

    @Override
    public ResponseResult saveCommentRepay(CommentRepaySaveDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || StringUtils.isBlank(dto.getContent()) || dto.getCommentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if(dto.getContent().length() > 140){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"璇勮鍐呭涓嶈兘瓒呰繃140瀛?);
        }

        //2.鍒ゆ柇鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.瀹夊叏妫€鏌?鑷瀹炵幇

        //4.淇濆瓨璇勮
        ApUser dbUser = userClient.findUserById(user.getId());
        if(dbUser == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"褰撳墠鐧诲綍淇℃伅鏈夎");
        }
        ApCommentRepay apCommentRepay = new ApCommentRepay();
        apCommentRepay.setAuthorId(user.getId());
        apCommentRepay.setContent(dto.getContent());
        apCommentRepay.setCreatedTime(new Date());
        apCommentRepay.setCommentId(dto.getCommentId());
        apCommentRepay.setAuthorName(dbUser.getName());
        apCommentRepay.setUpdatedTime(new Date());
        apCommentRepay.setLikes(0);
        mongoTemplate.save(apCommentRepay);

        //5鏇存柊鍥炲鏁伴噺
        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);
        apComment.setReply(apComment.getReply()+1);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult saveCommentRepayLike(CommentRepayLikeDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || dto.getCommentRepayId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鍒ゆ柇鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        ApCommentRepay apCommentRepay = mongoTemplate.findById(dto.getCommentRepayId(), ApCommentRepay.class);

        //3.鐐硅禐
        if(apCommentRepay != null && dto.getOperation() == 0){
            //鏇存柊璇勮鐐硅禐鏁伴噺
            apCommentRepay.setLikes(apCommentRepay.getLikes()+1);
            mongoTemplate.save(apCommentRepay);

            //淇濆瓨璇勮鐐硅禐鏁版嵁
            ApCommentRepayLike apCommentRepayLike = new ApCommentRepayLike();
            apCommentRepayLike.setCommentRepayId(apCommentRepay.getId());
            apCommentRepayLike.setAuthorId(user.getId());
            mongoTemplate.save(apCommentRepayLike);
        }else {
            //鏇存柊璇勮鐐硅禐鏁伴噺
            int tmp = apCommentRepay.getLikes()-1;
            tmp = tmp < 1 ? 0 : tmp;
            apCommentRepay.setLikes(tmp);
            mongoTemplate.save(apCommentRepay);

            //鍒犻櫎璇勮鐐硅禐
            Query query = Query.query(Criteria.where("commentRepayId").is(apCommentRepay.getId()).and("authorId").is(user.getId()));
            mongoTemplate.remove(query,ApCommentRepayLike.class);
        }

        //4.鍙栨秷鐐硅禐
        Map<String,Object> result = new HashMap<>();
        result.put("likes",apCommentRepay.getLikes());
        return ResponseResult.okResult(result);
    }
}

