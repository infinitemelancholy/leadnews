package com.leadnews.apis.schedule;

import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.schedule.dtos.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient("leadnews-schedule")
public interface IScheduleClient {

    /**
     * 娣诲姞寤惰繜浠诲姟
     * @param task
     * @return
     */
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task);

    /**
     * 鍙栨秷浠诲姟
     * @param taskId
     * @return
     */
    @GetMapping("/api/v1/task/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId);

    /**
     * 鎸夌収绫诲瀷鍜屼紭鍏堢骇鎷夊彇浠诲姟
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    public ResponseResult poll(@PathVariable("type") int type,@PathVariable("priority") int priority);
}

