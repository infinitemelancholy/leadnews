package com.leadnews.model.search.dtos;

import lombok.Data;

import java.util.Date;


@Data
public class UserSearchDto {

    /**
    * 鎼滅储鍏抽敭瀛?
    */
    String searchWords;
    /**
    * 褰撳墠椤?
    */
    int pageNum;
    /**
    * 鍒嗛〉鏉℃暟
    */
    int pageSize;
    /**
    * 鏈€灏忔椂闂?
    */
    Date minBehotTime;

    public int getFromIndex(){
        if(this.pageNum<1)return 0;
        if(this.pageSize<1) this.pageSize = 10;
        return this.pageSize * (pageNum-1);
    }
}
