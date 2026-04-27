package com.leadnews.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.leadnews.common.constants.ScheduleConstants;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.schedule.dtos.Task;
import com.leadnews.model.schedule.pojos.Taskinfo;
import com.leadnews.model.schedule.pojos.TaskinfoLogs;
import com.leadnews.schedule.mapper.TaskinfoLogsMapper;
import com.leadnews.schedule.mapper.TaskinfoMapper;
import com.leadnews.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {
    /**
     * 娣诲姞寤惰繜浠诲姟
     *
     * @param task
     * @return
     */
    @Override
    public long addTask(Task task) {
        //1.娣诲姞浠诲姟鍒版暟鎹簱涓?

        boolean success = addTaskToDb(task);

        if (success) {
            //2.娣诲姞浠诲姟鍒皉edis
            addTaskToCache(task);
        }


        return task.getTaskId();
    }

    @Autowired
    private CacheService cacheService;

    /**
     * 鎶婁换鍔℃坊鍔犲埌redis涓?
     *
     * @param task
     */
    private void addTaskToCache(Task task) {

        String key = task.getTaskType() + "_" + task.getPriority();

        //鑾峰彇5鍒嗛挓涔嬪悗鐨勬椂闂? 姣鍊?
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        //2.1 濡傛灉浠诲姟鐨勬墽琛屾椂闂村皬浜庣瓑浜庡綋鍓嶆椂闂达紝瀛樺叆list
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            //2.2 濡傛灉浠诲姟鐨勬墽琛屾椂闂村ぇ浜庡綋鍓嶆椂闂?&& 灏忎簬绛変簬棰勮鏃堕棿锛堟湭鏉?鍒嗛挓锛?瀛樺叆zset涓?
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }


    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;

    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    /**
     * 娣诲姞浠诲姟鍒版暟鎹簱涓?
     *
     * @param task
     * @return
     */
    private boolean addTaskToDb(Task task) {

        boolean flag = false;

        try {
            //淇濆瓨浠诲姟琛?
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);

            //璁剧疆taskID
            task.setTaskId(taskinfo.getTaskId());

            //淇濆瓨浠诲姟鏃ュ織鏁版嵁
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo, taskinfoLogs);
            taskinfoLogs.setVersion(1);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);

            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }


    /**
     * 鍙栨秷浠诲姟
     *
     * @param taskId
     * @return
     */
    @Override
    public boolean cancelTask(long taskId) {

        boolean flag = false;

        //鍒犻櫎浠诲姟锛屾洿鏂颁换鍔℃棩蹇?
        Task task = updateDb(taskId, ScheduleConstants.CANCELLED);

        //鍒犻櫎redis鐨勬暟鎹?
        if (task != null) {
            removeTaskFromCache(task);
            flag = true;

        }
        return flag;
    }

    /**
     * 鍒犻櫎redis涓殑鏁版嵁
     *
     * @param task
     */
    private void removeTaskFromCache(Task task) {

        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        } else {
            cacheService.zRemove(ScheduleConstants.FUTURE + key, JSON.toJSONString(task));
        }

    }

    /**
     * 鍒犻櫎浠诲姟锛屾洿鏂颁换鍔℃棩蹇?
     *
     * @param taskId
     * @param status
     * @return
     */
    private Task updateDb(long taskId, int status) {

        Task task = null;

        try {
            //鍒犻櫎浠诲姟
            taskinfoMapper.deleteById(taskId);

            //鏇存柊浠诲姟鏃ュ織
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);

            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs, task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        } catch (Exception e) {
            log.error("task cancel exception taskId={}", taskId);
        }


        return task;
    }

    /**
     * 鎸夌収绫诲瀷鍜屼紭鍏堢骇鎷夊彇浠诲姟
     *
     * @param type
     * @param priority
     * @return
     */
    @Override
    public Task poll(int type, int priority) {
        Task task = null;

        try {
            String key = type + "_" + priority;

            //浠巖edis涓媺鍙栨暟鎹? pop
            String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if (StringUtils.isNotBlank(task_json)) {
                task = JSON.parseObject(task_json, Task.class);

                //淇敼鏁版嵁搴撲俊鎭?
                updateDb(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("poll task exception");
        }


        return task;
    }

    /**
     * 鏈潵鏁版嵁瀹氭椂鍒锋柊
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {

        String token = cacheService.tryLock("FUTRUE_TASK_SYNC", 1000 * 30);

        if(StringUtils.isNotBlank(token)){
            log.info("鏈潵鏁版嵁瀹氭椂鍒锋柊---瀹氭椂浠诲姟");

            //鑾峰彇鎵€鏈夋湭鏉ユ暟鎹殑闆嗗悎key
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys) {//future_100_50

                //鑾峰彇褰撳墠鏁版嵁鐨刱ey  topic
                String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];

                //鎸夌収key鍜屽垎鍊兼煡璇㈢鍚堟潯浠剁殑鏁版嵁
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());

                //鍚屾鏁版嵁
                if (!tasks.isEmpty()) {
                    cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                    log.info("鎴愬姛鐨勫皢" + futureKey + "鍒锋柊鍒颁簡" + topicKey);
                }
            }
        }



    }


}

