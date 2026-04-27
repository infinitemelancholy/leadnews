package com.leadnews.es.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class SearchArticleVo {

    // 鏂囩珷id
    private Long id;
    // 鏂囩珷鏍囬
    private String title;
    // 鏂囩珷鍙戝竷鏃堕棿
    private Date publishTime;
    // 鏂囩珷甯冨眬
    private Integer layout;
    // 灏侀潰
    private String images;
    // 浣滆€卛d
    private Long authorId;
    // 浣滆€呭悕璇?
    private String authorName;
    //闈欐€乽rl
    private String staticUrl;
    //鏂囩珷鍐呭
    private String content;

}
