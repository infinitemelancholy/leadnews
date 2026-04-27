package com.leadnews.freemarker.test;


import com.leadnews.freemarker.FreemarkerDemoApplication;
import com.leadnews.freemarker.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SpringBootTest(classes = FreemarkerDemoApplication.class)
@RunWith(SpringRunner.class)
public class FreemarkerTest {


    @Autowired
    private Configuration configuration;

    @Test
    public void test() throws IOException, TemplateException {
        Template template = configuration.getTemplate("02-list.ftl");

        /**
         * 鍚堟垚鏂规硶
         *
         * 涓や釜鍙傛暟
         * 绗竴涓弬鏁帮細妯″瀷鏁版嵁
         * 绗簩涓弬鏁帮細杈撳嚭娴?
         */
        template.process(getData(),new FileWriter("d:/list.html"));
    }


    private Map getData(){

        Map<String,Object> map = new HashMap<>();

        //------------------------------------
        Student stu1 = new Student();
        stu1.setName("灏忓己");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());

        //灏忕孩瀵硅薄妯″瀷鏁版嵁
        Student stu2 = new Student();
        stu2.setName("灏忕孩");
        stu2.setMoney(200.1f);
        stu2.setAge(19);

        //灏嗕袱涓璞℃ā鍨嬫暟鎹瓨鏀惧埌List闆嗗悎涓?
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);

        //鍚憁odel涓瓨鏀綥ist闆嗗悎鏁版嵁
        map.put("stus",stus);


        //map鏁版嵁
        Map<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);

        map.put("stuMap",stuMap);
        //鏃ユ湡
        map.put("today",new Date());

        //闀挎暟鍊?
        map.put("point",38473897438743L);

        return map;

    }
}

