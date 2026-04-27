package com.leadnews.kafka.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * 娴佸紡澶勭悊
 */
public class KafkaStreamQuickStart {

    public static void main(String[] args) {

        //kafka鐨勯厤缃俊蹇?
        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"streams-quickstart");

        //stream 鏋勫缓鍣?
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        //娴佸紡璁＄畻
        streamProcessor(streamsBuilder);


        //鍒涘缓kafkaStream瀵硅薄
        KafkaStreams kafkaStreams = new KafkaStreams(streamsBuilder.build(),prop);
        //寮€鍚祦寮忚绠?
        kafkaStreams.start();
    }

    /**
     * 娴佸紡璁＄畻
     *
     * 娑堟伅鐨勫唴瀹?hello kafka
     *
     * @param streamsBuilder
     */
    private static void streamProcessor(StreamsBuilder streamsBuilder) {
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
    }
}

