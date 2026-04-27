package com.leadnews.search.service;

import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.search.dtos.UserSearchDto;

import java.io.IOException;

public interface ArticleSearchService {

    /**
     * es鏂囩珷鍒嗛〉妫€绱?
     * @param dto
     * @return
     */
    public ResponseResult search(UserSearchDto dto) throws IOException;
}

