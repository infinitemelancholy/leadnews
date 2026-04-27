package com.leadnews.comment.service.impl;

import com.leadnews.apis.user.IUserClient;
import com.leadnews.comment.pojos.ApComment;
import com.leadnews.comment.pojos.ApCommentLike;
import com.leadnews.comment.pojos.CommentVo;
import com.leadnews.comment.service.CommentHotService;
import com.leadnews.comment.service.CommentService;
import com.leadnews.model.comment.dtos.CommentDto;
import com.leadnews.model.comment.dtos.CommentLikeDto;
import com.leadnews.model.comment.dtos.CommentSaveDto;
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
public class CommentServiceImpl implements CommentService {

    @Autowired
    private IUserClient userClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ResponseResult saveComment(CommentSaveDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || StringUtils.isBlank(dto.getContent()) || dto.getArticleId() == null){
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
        ApComment apComment = new ApComment();
        apComment.setAuthorId(user.getId());
        apComment.setContent(dto.getContent());
        apComment.setCreatedTime(new Date());
        apComment.setEntryId(dto.getArticleId());
        apComment.setImage(dbUser.getImage());
        apComment.setAuthorName(dbUser.getName());
        apComment.setLikes(0);
        apComment.setReply(0);
        apComment.setType((short)0);
        apComment.setFlag((short)0);
        mongoTemplate.save(apComment);

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Autowired
    private CommentHotService commentHotService;

    @Override
    public ResponseResult like(CommentLikeDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || dto.getCommentId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鍒ゆ柇鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        ApComment apComment = mongoTemplate.findById(dto.getCommentId(), ApComment.class);

        //3.鐐硅禐
        if(apComment != null && dto.getOperation() == 0){
            //鏇存柊璇勮鐐硅禐鏁伴噺
            apComment.setLikes(apComment.getLikes()+1);
            mongoTemplate.save(apComment);

            //璁＄畻鐑偣璇勮
//            if(apComment.getFlag().shortValue() == 0 && apComment.getLikes() >= 5){
//                commentHotService.findHotComment(apComment.getEntryId(),apComment);
//            }

            //淇濆瓨璇勮鐐硅禐鏁版嵁
            ApCommentLike apCommentLike = new ApCommentLike();
            apCommentLike.setCommentId(apComment.getId());
            apCommentLike.setAuthorId(user.getId());
            mongoTemplate.save(apCommentLike);
        }else {
            //鏇存柊璇勮鐐硅禐鏁伴噺
            int tmp = apComment.getLikes()-1;
            tmp = tmp < 1 ? 0 : tmp;
            apComment.setLikes(tmp);
            mongoTemplate.save(apComment);

            //鍒犻櫎璇勮鐐硅禐
            Query query = Query.query(Criteria.where("commentId").is(apComment.getId()).and("authorId").is(user.getId()));
            mongoTemplate.remove(query,ApCommentLike.class);
        }

        //4.鍙栨秷鐐硅禐
        Map<String,Object> result = new HashMap<>();
        result.put("likes",apComment.getLikes());
        return ResponseResult.okResult(result);
    }

    @Override
    public ResponseResult findByArticleId(CommentDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto == null || dto.getArticleId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        int size = 10;

        //2.鍔犺浇鏁版嵁
        Query query = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("createdTime").lt(dto.getMinDate()));
        query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);

        /*List<ApComment> list = null;
        if(dto.getIndex() == 1){
            //棣栭〉
            Query query = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("flag").is(1));
            query.with(Sort.by(Sort.Direction.DESC,"likes"));
            list = mongoTemplate.find(query, ApComment.class);
            if(list != null && list.size() > 0){
                //琛ュ叏棣栭〉鐨勬暟鎹?
                size = size - list.size();
                Query query1 = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("flag").is(0).and("createdTime").lt(dto.getMinDate()));
                query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
                List<ApComment> list2 = mongoTemplate.find(query1, ApComment.class);
                list.addAll(list2);
            }else {
                Query query2 = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("flag").is(0).and("createdTime").lt(dto.getMinDate()));
                query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
                list = mongoTemplate.find(query2, ApComment.class);
            }

        }else {
            //涓嶆槸棣栭〉
            Query query = Query.query(Criteria.where("entryId").is(dto.getArticleId()).and("flag").is(0).and("createdTime").lt(dto.getMinDate()));
            query.with(Sort.by(Sort.Direction.DESC,"createdTime")).limit(size);
            list = mongoTemplate.find(query, ApComment.class);
        }*/

        //3.鏁版嵁灏佽杩斿洖
        //3.1 鐢ㄦ埛鏈櫥褰?
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.okResult(list);
        }

        //3.2 鐢ㄦ埛宸茬櫥褰?

        //闇€瑕佹煡璇㈠綋鍓嶈瘎璁轰腑鍝簺鏁版嵁琚偣璧炰簡
        List<String> idList = list.stream().map(x -> x.getId()).collect(Collectors.toList());
        Query query1 = Query.query(Criteria.where("commentId").in(idList).and("authorId").is(user.getId()));
        List<ApCommentLike> apCommentLikes = mongoTemplate.find(query1, ApCommentLike.class);
        if(apCommentLikes == null){
            return ResponseResult.okResult(list);
        }

        List<CommentVo> resultList = new ArrayList<>();
        list.forEach(x->{
            CommentVo vo = new CommentVo();
            BeanUtils.copyProperties(x,vo);
            for (ApCommentLike apCommentLike : apCommentLikes) {
                if(x.getId().equals(apCommentLike.getCommentId())){
                    vo.setOperation((short)0);
                    break;
                }
            }
            resultList.add(vo);
        });

        return ResponseResult.okResult(resultList);
    }
}

