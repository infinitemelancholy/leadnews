package com.leadnews.schedule.service;

import com.leadnews.model.schedule.dtos.Task;

public interface TaskService {


    /**
     * 娣诲姞寤惰繜浠诲姟
     * @param task
     * @return
     */
    public long addTask(Task task);

    /**
     * 鍙栨秷浠诲姟
     * @param taskId
     * @return
     */
    public boolean cancelTask(long taskId);

    /**
     * 鎸夌収绫诲瀷鍜屼紭鍏堢骇鎷夊彇浠诲姟
     * @param type
     * @param priority
     * @return
     */
    public Task poll(int type,int priority);


}

