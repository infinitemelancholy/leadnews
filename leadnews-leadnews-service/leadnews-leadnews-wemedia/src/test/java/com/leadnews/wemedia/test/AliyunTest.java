package com.leadnews.wemedia.test;


import com.leadnews.common.aliyun.GreenImageScan;
import com.leadnews.common.aliyun.GreenTextScan;
import com.leadnews.file.service.FileStorageService;
import com.leadnews.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private GreenImageScan greenImageScan;

    /**
     * 娴嬭瘯鏂囨湰鍐呭瀹℃牳
     */
    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan.greeTextScan("鎴戞槸涓€涓ソ浜?鍐版瘨");
        System.out.println(map);
    }

    /**
     * 娴嬭瘯鍥剧墖瀹℃牳
     */
    @Test
    public void testScanImage() throws Exception {

        byte[] bytes = fileStorageService.downLoadFile("http://192.168.200.130:9000/leadnews/2021/04/26/07caf2be1520457e9fe59f2969eebf65.jpg");

        List<byte []> list = new ArrayList<>();
        list.add(bytes);

        Map map = greenImageScan.imageScan(list);
        System.out.println(map);

    }
}

