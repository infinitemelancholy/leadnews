package com.leadnews.utils.thread;

import com.leadnews.model.user.pojos.ApUser;

public class AppThreadLocalUtil {

    private final static ThreadLocal<ApUser> WM_USER_THREAD_LOCAL = new ThreadLocal<>();

    //瀛樺叆绾跨▼涓?
    public static void setUser(ApUser apUser){
        WM_USER_THREAD_LOCAL.set(apUser);
    }

    //浠庣嚎绋嬩腑鑾峰彇
    public static ApUser getUser(){
        return WM_USER_THREAD_LOCAL.get();
    }

    //娓呯悊
    public static void clear(){
        WM_USER_THREAD_LOCAL.remove();
    }

}

