package com.leadnews.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.apis.schedule.IScheduleClient;
import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.TaskTypeEnum;
import com.leadnews.model.schedule.dtos.Task;
import com.leadnews.model.wemedia.pojos.WmNews;
import com.leadnews.utils.common.ProtostuffUtil;
import com.leadnews.wemedia.service.WmNewsAutoScanService;
import com.leadnews.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;

    /**
     * 娣诲姞浠诲姟鍒板欢杩熼槦鍒椾腑
     * @param id          鏂囩珷鐨刬d
     * @param publishTime 鍙戝竷鐨勬椂闂? 鍙互鍋氫负浠诲姟鐨勬墽琛屾椂闂?
     */
    @Override
    @Async
    public void addNewsToTask(Integer id, Date publishTime) {

        log.info("娣诲姞浠诲姟鍒板欢杩熸湇鍔′腑----begin");

        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(ProtostuffUtil.serialize(wmNews));

        scheduleClient.addTask(task);

        log.info("娣诲姞浠诲姟鍒板欢杩熸湇鍔′腑----end");

    }

    @Autowired
    private WmNewsAutoScanService wmNewsAutoScanService;

    /**
     * 娑堣垂浠诲姟锛屽鏍告枃绔?
     */
    @Scheduled(fixedRate = 1000)
    @Override
    public void scanNewsByTask() {

        log.info("娑堣垂浠诲姟锛屽鏍告枃绔?);

        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if(responseResult.getCode().equals(200) && responseResult.getData() != null){
            Task task = JSON.parseObject(JSON.toJSONString(responseResult.getData()), Task.class);
            WmNews wmNews = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
            wmNewsAutoScanService.autoScanWmNews(wmNews.getId());

        }
    }
}

