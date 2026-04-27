package com.leadnews.search.service;

import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.search.dtos.UserSearchDto;

public interface ApAssociateWordsService {

    /**
     * 鎼滅储鑱旀兂璇?
     * @param dto
     * @return
     */
    public ResponseResult search(UserSearchDto dto);
}

