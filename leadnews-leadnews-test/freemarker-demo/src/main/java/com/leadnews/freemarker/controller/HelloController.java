package com.leadnews.freemarker.controller;

import com.leadnews.freemarker.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;


@Controller
public class HelloController {

    @GetMapping("/basic")
    public String hello(Model model){

        //name
//        model.addAttribute("name","freemarker");
        //stu
        Student student = new Student();
        student.setName("灏忔槑");
        student.setAge(18);
        model.addAttribute("stu",student);

        return "01-basic";
    }

    @GetMapping("/list")
    public String list(Model model){
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
        model.addAttribute("stus",stus);

        //map鏁版嵁
        Map<String,Student> stuMap = new HashMap<>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);

        model.addAttribute("stuMap",stuMap);
        //鏃ユ湡
        model.addAttribute("today",new Date());

        //闀挎暟鍊?
        model.addAttribute("point",38473897438743L);

        return "02-list";
    }

}

