package com.leadnews.model.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskTypeEnum {

    NEWS_SCAN_TIME(1001, 1,"鏂囩珷瀹氭椂瀹℃牳"),
    REMOTEERROR(1002, 2,"绗笁鏂规帴鍙ｈ皟鐢ㄥけ璐ワ紝閲嶈瘯");
    private final int taskType; //瀵瑰簲鍏蜂綋涓氬姟
    private final int priority; //涓氬姟涓嶅悓绾у埆
    private final String desc; //鎻忚堪淇℃伅
}
