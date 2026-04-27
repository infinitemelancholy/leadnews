package com.leadnews.search.service.impl;

import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import com.leadnews.model.search.dtos.UserSearchDto;
import com.leadnews.search.pojos.ApAssociateWords;
import com.leadnews.search.service.ApAssociateWordsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ApAssociateWordsServiceImpl implements ApAssociateWordsService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 鎼滅储鑱旀兂璇?
     * @param dto
     * @return
     */
    @Override
    public ResponseResult search(UserSearchDto dto) {
        //1.妫€鏌ュ弬鏁?
        if(StringUtils.isBlank(dto.getSearchWords())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //2.鍒嗛〉妫€鏌?
        if(dto.getPageSize() > 20){
            dto.setPageSize(20);
        }

        //3.鎵ц鏌ヨ锛屾ā绯婃煡璇?
        Query query = Query.query(Criteria.where("associateWords").regex(".*?\\" + dto.getSearchWords() + ".*"));
        query.limit(dto.getPageSize());
        List<ApAssociateWords> apAssociateWords = mongoTemplate.find(query, ApAssociateWords.class);
        return ResponseResult.okResult(apAssociateWords);
    }
}

