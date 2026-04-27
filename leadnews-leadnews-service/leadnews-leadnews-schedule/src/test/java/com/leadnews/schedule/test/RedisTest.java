package com.leadnews.schedule.test;


import com.alibaba.fastjson.JSON;
import com.leadnews.common.redis.CacheService;
import com.leadnews.model.schedule.dtos.Task;
import com.leadnews.schedule.ScheduleApplication;
import com.sun.istack.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Set;


@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void testList(){

        //鍦╨ist鐨勫乏杈规坊鍔犲厓绱?
//        cacheService.lLeftPush("list_001","hello,redis");

        //鍦╨ist鐨勫彸杈硅幏鍙栧厓绱狅紝骞跺垹闄?
        String list_001 = cacheService.lRightPop("list_001");
        System.out.println(list_001);
    }

    @Test
    public void testZset(){
        //娣诲姞鏁版嵁鍒皕set涓? 鍒嗗€?
        /*cacheService.zAdd("zset_key_001","hello zset 001",1000);
        cacheService.zAdd("zset_key_001","hello zset 002",8888);
        cacheService.zAdd("zset_key_001","hello zset 003",7777);
        cacheService.zAdd("zset_key_001","hello zset 004",999999);*/

        //鎸夌収鍒嗗€艰幏鍙栨暟鎹?
        Set<String> zset_key_001 = cacheService.zRangeByScore("zset_key_001", 0, 8888);
        System.out.println(zset_key_001);
    }

    @Test
    public void testKeys(){
        Set<String> keys = cacheService.keys("future_*");
        System.out.println(keys);

        Set<String> scan = cacheService.scan("future_*");
        System.out.println(scan);
    }

    //鑰楁椂6151
    @Test
    public  void testPiple1(){
        long start =System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            Task task = new Task();
            task.setTaskType(1001);
            task.setPriority(1);
            task.setExecuteTime(new Date().getTime());
            cacheService.lLeftPush("1001_1", JSON.toJSONString(task));
        }
        System.out.println("鑰楁椂"+(System.currentTimeMillis()- start));
    }


    @Test
    public void testPiple2(){
        long start  = System.currentTimeMillis();
        //浣跨敤绠￠亾鎶€鏈?
        List<Object> objectList = cacheService.getstringRedisTemplate().executePipelined(new RedisCallback<Object>() {
            @Nullable
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (int i = 0; i <10000 ; i++) {
                    Task task = new Task();
                    task.setTaskType(1001);
                    task.setPriority(1);
                    task.setExecuteTime(new Date().getTime());
                    redisConnection.lPush("1001_1".getBytes(), JSON.toJSONString(task).getBytes());
                }
                return null;
            }
        });
        System.out.println("浣跨敤绠￠亾鎶€鏈墽琛?0000娆¤嚜澧炴搷浣滃叡鑰楁椂:"+(System.currentTimeMillis()-start)+"姣");
    }

}

