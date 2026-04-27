package com.leadnews.comment.service.impl;

import com.leadnews.comment.pojos.ApComment;
import com.leadnews.comment.service.CommentHotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentHotServiceImpl implements CommentHotService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Async//浣跨敤寮傛绾跨▼姹?
    public void findHotComment(Long entryId, ApComment apComment) {
        Query query = Query.query(Criteria.where("entryId").is(entryId).and("flag").is(1)).with(Sort.by(Sort.Direction.DESC, "likes"));
        List<ApComment> list = mongoTemplate.find(query, ApComment.class);
        if (list != null && list.size() > 0) {
            ApComment hotComment = list.get(list.size() - 1);
            if (apComment.getLikes() > hotComment.getLikes()) {
                //淇敼褰撳墠鏁版嵁涓虹儹鐐规暟鎹?
                apComment.setFlag((short) 1);
                mongoTemplate.save(apComment);
                if(list.size() >= 5){
                    //淇敼鏈€鍚庝竴鏉＄儹鐐规暟鎹负鏅€氳瘎璁?
                    hotComment.setFlag((short) 0);
                    mongoTemplate.save(hotComment);
                }
            }

        } else {
            apComment.setFlag((short) 1);
            mongoTemplate.save(apComment);
        }


        /*if (list != null && list.size() == 5) {
            ApComment hotComment = list.get(list.size() - 1);
            if (apComment.getLikes() > hotComment.getLikes()) {
                //淇敼褰撳墠鏁版嵁涓虹儹鐐规暟鎹?
                apComment.setFlag((short) 1);
                mongoTemplate.save(apComment);
                //淇敼鏈€鍚庝竴鏉＄儹鐐规暟鎹负鏅€氳瘎璁?
                hotComment.setFlag((short) 0);
                mongoTemplate.save(hotComment);
            }

        } else {
            apComment.setFlag((short) 1);
            mongoTemplate.save(apComment);
        }*/

    }
}

