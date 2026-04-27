package com.leadnews.search.service;

import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.search.dtos.HistorySearchDto;

public interface ApUserSearchService {

    /**
     * 淇濆瓨鐢ㄦ埛鎼滅储鍘嗗彶璁板綍
     * @param keyword
     * @param userId
     */
    public void insert(String keyword,Integer userId);

    /**
     * 鏌ヨ鎼滅储鍘嗗彶
     * @return
     */
    public ResponseResult findUserSearch();

    /**
     * 鍒犻櫎鍘嗗彶璁板綍
     * @param dto
     * @return
     */
    public ResponseResult delUserSearch(HistorySearchDto dto);
}

