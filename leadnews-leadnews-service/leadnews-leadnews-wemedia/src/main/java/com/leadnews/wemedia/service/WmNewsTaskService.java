package com.leadnews.wemedia.service;

import java.util.Date;

public interface WmNewsTaskService {

    /**
     * 娣诲姞浠诲姟鍒板欢杩熼槦鍒椾腑
     * @param id  鏂囩珷鐨刬d
     * @param publishTime  鍙戝竷鐨勬椂闂? 鍙互鍋氫负浠诲姟鐨勬墽琛屾椂闂?
     */
    public void addNewsToTask(Integer id, Date publishTime);


    /**
     * 娑堣垂浠诲姟锛屽鏍告枃绔?
     */
    public void scanNewsByTask();

}

