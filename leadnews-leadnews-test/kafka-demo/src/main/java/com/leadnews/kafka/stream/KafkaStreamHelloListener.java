package com.leadnews.kafka.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@Slf4j
public class KafkaStreamHelloListener {

    @Bean
    public KStream<String,String> kStream(StreamsBuilder streamsBuilder){
        //鍒涘缓kstream瀵硅薄锛屽悓鏃舵寚瀹氫粠閭ｄ釜topic涓帴鏀舵秷鎭?
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");
        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String value) {
                return Arrays.asList(value.split(" "));
            }
        })
                //鏍规嵁value杩涜鑱氬悎鍒嗙粍
                .groupBy((key,value)->value)
                //鑱氬悎璁＄畻鏃堕棿闂撮殧
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                //姹傚崟璇嶇殑涓暟
                .count()
                .toStream()
                //澶勭悊鍚庣殑缁撴灉杞崲涓簊tring瀛楃涓?
                .map((key,value)->{
                    System.out.println("key:"+key+",value:"+value);
                    return new KeyValue<>(key.key().toString(),value.toString());
                })
                //鍙戦€佹秷鎭?
                .to("itcast-topic-out");
        return stream;
    }
}

