package com.leadnews.mongo.test;


import com.leadnews.mongo.MongoApplication;
import com.leadnews.mongo.pojo.ApAssociateWords;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = MongoApplication.class)
@RunWith(SpringRunner.class)
public class MongoTest {


    @Autowired
    private MongoTemplate mongoTemplate;

    //淇濆瓨
    @Test
    public void saveTest(){
        /*for (int i = 0; i < 10; i++) {
            ApAssociateWords apAssociateWords = new ApAssociateWords();
            apAssociateWords.setAssociateWords("榛戦┈澶存潯");
            apAssociateWords.setCreatedTime(new Date());
            mongoTemplate.save(apAssociateWords);
        }*/
        ApAssociateWords apAssociateWords = new ApAssociateWords();
        apAssociateWords.setAssociateWords("榛戦┈鐩存挱");
        apAssociateWords.setCreatedTime(new Date());
        mongoTemplate.save(apAssociateWords);

    }

    //鏌ヨ涓€涓?
    @Test
    public void saveFindOne(){
        ApAssociateWords apAssociateWords = mongoTemplate.findById("60bd973eb0c1d430a71a7928", ApAssociateWords.class);
        System.out.println(apAssociateWords);
    }

    //鏉′欢鏌ヨ
    @Test
    public void testQuery(){
        Query query = Query.query(Criteria.where("associateWords").is("榛戦┈澶存潯"))
                .with(Sort.by(Sort.Direction.DESC,"createdTime"));
        List<ApAssociateWords> apAssociateWordsList = mongoTemplate.find(query, ApAssociateWords.class);
        System.out.println(apAssociateWordsList);
    }

    @Test
    public void testDel(){
        mongoTemplate.remove(Query.query(Criteria.where("associateWords").is("榛戦┈澶存潯")),ApAssociateWords.class);
    }
}

