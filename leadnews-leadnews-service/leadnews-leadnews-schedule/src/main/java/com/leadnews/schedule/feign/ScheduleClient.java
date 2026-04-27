package com.leadnews.schedule.feign;

import com.leadnews.apis.schedule.IScheduleClient;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.schedule.dtos.Task;
import com.leadnews.schedule.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ScheduleClient implements IScheduleClient {

    @Autowired
    private TaskService taskService;
    /**
     * 娣诲姞寤惰繜浠诲姟
     *
     * @param task
     * @return
     */
    @PostMapping("/api/v1/task/add")
    public ResponseResult addTask(@RequestBody Task task) {
        return ResponseResult.okResult(taskService.addTask(task));
    }

    /**
     * 鍙栨秷浠诲姟
     *
     * @param taskId
     * @return
     */
    @GetMapping("/api/v1/task/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId){
        return ResponseResult.okResult(taskService.cancelTask(taskId));
    }

    /**
     * 鎸夌収绫诲瀷鍜屼紭鍏堢骇鎷夊彇浠诲姟
     *
     * @param type
     * @param priority
     * @return
     */
    @GetMapping("/api/v1/task/{type}/{priority}")
    public ResponseResult poll(@PathVariable("type") int type,@PathVariable("priority") int priority) {
        return ResponseResult.okResult(taskService.poll(type,priority));
    }
}

