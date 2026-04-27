package com.leadnews.utils.thread;

import com.leadnews.model.wemedia.pojos.WmUser;

public class WmThreadLocalUtil {

    private final static ThreadLocal<WmUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    //瀛樺叆绾跨▼涓?
    public static void setUser(WmUser wmUser){
        WM_USER_THREAD_LOCAL.set(wmUser);
    }

    //浠庣嚎绋嬩腑鑾峰彇
    public static WmUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    //娓呯悊
    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}

