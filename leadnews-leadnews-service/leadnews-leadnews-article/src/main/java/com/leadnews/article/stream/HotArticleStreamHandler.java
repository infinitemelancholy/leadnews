package com.leadnews.article.stream;

import com.alibaba.fastjson.JSON;
import com.leadnews.common.constants.HotArticleConstants;
import com.leadnews.model.mess.ArticleVisitStreamMess;
import com.leadnews.model.mess.UpdateArticleMess;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class HotArticleStreamHandler {

    @Bean
    public KStream<String,String> kStream(StreamsBuilder streamsBuilder){
        //鎺ユ敹娑堟伅
        KStream<String,String> stream = streamsBuilder.stream(HotArticleConstants.HOT_ARTICLE_SCORE_TOPIC);
        //鑱氬悎娴佸紡澶勭悊
        stream.map((key,value)->{
            UpdateArticleMess mess = JSON.parseObject(value, UpdateArticleMess.class);
            //閲嶇疆娑堟伅鐨刱ey:1234343434   鍜? value: likes:1
            return new KeyValue<>(mess.getArticleId().toString(),mess.getType().name()+":"+mess.getAdd());
        })
                //鎸夌収鏂囩珷id杩涜鑱氬悎
                .groupBy((key,value)->key)
                //鏃堕棿绐楀彛
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                /**
                 * 鑷鐨勫畬鎴愯仛鍚堢殑璁＄畻
                 */
                .aggregate(new Initializer<String>() {
                    /**
                     * 鍒濆鏂规硶锛岃繑鍥炲€兼槸娑堟伅鐨剉alue
                     * @return
                     */
                    @Override
                    public String apply() {
                        return "COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0";
                    }
                    /**
                     * 鐪熸鐨勮仛鍚堟搷浣滐紝杩斿洖鍊兼槸娑堟伅鐨剉alue
                     */
                }, new Aggregator<String, String, String>() {
                    @Override
                    public String apply(String key, String value, String aggValue) {
                        if(StringUtils.isBlank(value)){
                            return aggValue;
                        }
                        String[] aggAry = aggValue.split(",");
                        int col = 0,com=0,lik=0,vie=0;
                        for (String agg : aggAry) {
                            String[] split = agg.split(":");
                            /**
                             * 鑾峰緱鍒濆鍊硷紝涔熸槸鏃堕棿绐楀彛鍐呰绠椾箣鍚庣殑鍊?
                             */
                            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])){
                                case COLLECTION:
                                    col = Integer.parseInt(split[1]);
                                    break;
                                case COMMENT:
                                    com = Integer.parseInt(split[1]);
                                    break;
                                case LIKES:
                                    lik = Integer.parseInt(split[1]);
                                    break;
                                case VIEWS:
                                    vie = Integer.parseInt(split[1]);
                                    break;
                            }
                        }
                        /**
                         * 绱姞鎿嶄綔
                         */
                        String[] valAry = value.split(":");
                        switch (UpdateArticleMess.UpdateArticleType.valueOf(valAry[0])){
                            case COLLECTION:
                                col += Integer.parseInt(valAry[1]);
                                break;
                            case COMMENT:
                                com += Integer.parseInt(valAry[1]);
                                break;
                            case LIKES:
                                lik += Integer.parseInt(valAry[1]);
                                break;
                            case VIEWS:
                                vie += Integer.parseInt(valAry[1]);
                                break;
                        }

                        String formatStr = String.format("COLLECTION:%d,COMMENT:%d,LIKES:%d,VIEWS:%d", col, com, lik, vie);
                        System.out.println("鏂囩珷鐨刬d:"+key);
                        System.out.println("褰撳墠鏃堕棿绐楀彛鍐呯殑娑堟伅澶勭悊缁撴灉锛?+formatStr);
                        return formatStr;
                    }
                }, Materialized.as("hot-atricle-stream-count-001"))
                .toStream()
                .map((key,value)->{
                    return new KeyValue<>(key.key().toString(),formatObj(key.key().toString(),value));
                })
                //鍙戦€佹秷鎭?
                .to(HotArticleConstants.HOT_ARTICLE_INCR_HANDLE_TOPIC);

        return stream;


    }

    /**
     * 鏍煎紡鍖栨秷鎭殑value鏁版嵁
     * @param articleId
     * @param value
     * @return
     */
    public String formatObj(String articleId,String value){
        ArticleVisitStreamMess mess = new ArticleVisitStreamMess();
        mess.setArticleId(Long.valueOf(articleId));
        //COLLECTION:0,COMMENT:0,LIKES:0,VIEWS:0
        String[] valAry = value.split(",");
        for (String val : valAry) {
            String[] split = val.split(":");
            switch (UpdateArticleMess.UpdateArticleType.valueOf(split[0])){
                case COLLECTION:
                    mess.setCollect(Integer.parseInt(split[1]));
                    break;
                case COMMENT:
                    mess.setComment(Integer.parseInt(split[1]));
                    break;
                case LIKES:
                    mess.setLike(Integer.parseInt(split[1]));
                    break;
                case VIEWS:
                    mess.setView(Integer.parseInt(split[1]));
                    break;
            }
        }
        log.info("鑱氬悎娑堟伅澶勭悊涔嬪悗鐨勭粨鏋滀负:{}",JSON.toJSONString(mess));
        return JSON.toJSONString(mess);

    }
}

