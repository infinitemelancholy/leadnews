package com.leadnews.search.service.impl;

import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.search.dtos.HistorySearchDto;
import com.leadnews.model.user.pojos.ApUser;
import com.leadnews.search.pojos.ApUserSearch;
import com.leadnews.search.service.ApUserSearchService;
import com.leadnews.utils.thread.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ApUserSearchServiceImpl implements ApUserSearchService {

    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 淇濆瓨鐢ㄦ埛鎼滅储鍘嗗彶璁板綍
     * @param keyword
     * @param userId
     */
    @Override
    @Async
    public void insert(String keyword, Integer userId) {
        //1.鏌ヨ褰撳墠鐢ㄦ埛鐨勬悳绱㈠叧閿瘝
        Query query = Query.query(Criteria.where("userId").is(userId).and("keyword").is(keyword));
        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);

        //2.瀛樺湪 鏇存柊鍒涘缓鏃堕棿
        if(apUserSearch != null){
            apUserSearch.setCreatedTime(new Date());
            mongoTemplate.save(apUserSearch);
            return;
        }

        //3.涓嶅瓨鍦紝鍒ゆ柇褰撳墠鍘嗗彶璁板綍鎬绘暟閲忔槸鍚﹁秴杩?0
        apUserSearch = new ApUserSearch();
        apUserSearch.setUserId(userId);
        apUserSearch.setKeyword(keyword);
        apUserSearch.setCreatedTime(new Date());

        Query query1 = Query.query(Criteria.where("userId").is(userId));
        query1.with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApUserSearch> apUserSearchList = mongoTemplate.find(query1, ApUserSearch.class);

        if(apUserSearchList == null || apUserSearchList.size() < 10){
            mongoTemplate.save(apUserSearch);
        }else {
            ApUserSearch lastUserSearch = apUserSearchList.get(apUserSearchList.size() - 1);
            mongoTemplate.findAndReplace(Query.query(Criteria.where("id").is(lastUserSearch.getId())),apUserSearch);
        }


    }

    /**
     * 鏌ヨ鎼滅储鍘嗗彶
     *
     * @return
     */
    @Override
    public ResponseResult findUserSearch() {
        //鑾峰彇褰撳墠鐢ㄦ埛
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //鏍规嵁鐢ㄦ埛鏌ヨ鏁版嵁锛屾寜鐓ф椂闂村€掑簭
        List<ApUserSearch> apUserSearches = mongoTemplate.find(Query.query(Criteria.where("userId").is(user.getId())).with(Sort.by(Sort.Direction.DESC, "createdTime")), ApUserSearch.class);
        return ResponseResult.okResult(apUserSearches);
    }

    /**
     * 鍒犻櫎鍘嗗彶璁板綍
     *
     * @param dto
     * @return
     */
    @Override
    public ResponseResult delUserSearch(HistorySearchDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(dto.getId() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鍒ゆ柇鏄惁鐧诲綍
        ApUser user = AppThreadLocalUtil.getUser();
        if(user == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        //3.鍒犻櫎
        mongoTemplate.remove(Query.query(Criteria.where("userId").is(user.getId()).and("id").is(dto.getId())),ApUserSearch.class);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }
}

