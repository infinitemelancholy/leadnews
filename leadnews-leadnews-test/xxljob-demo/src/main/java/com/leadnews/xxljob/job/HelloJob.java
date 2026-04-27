package com.leadnews.xxljob.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HelloJob {

    @Value("${server.port}")
    private String port;


    @XxlJob("demoJobHandler")
    public void helloJob(){
        System.out.println("绠€鍗曚换鍔℃墽琛屼簡銆傘€傘€傘€?+port);

    }

    @XxlJob("shardingJobHandler")
    public void shardingJobHandler(){
        //鍒嗙墖鐨勫弬鏁?
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        //涓氬姟閫昏緫
        List<Integer> list = getList();
        for (Integer integer : list) {
            if(integer % shardTotal == shardIndex){
                System.out.println("褰撳墠绗?+shardIndex+"鍒嗙墖鎵ц浜嗭紝浠诲姟椤逛负锛?+integer);
            }
        }
    }

    public List<Integer> getList(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        return list;
    }
}

