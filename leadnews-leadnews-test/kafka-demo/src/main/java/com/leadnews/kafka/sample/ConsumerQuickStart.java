package com.leadnews.kafka.sample;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 娑堣垂鑰?
 */
public class ConsumerQuickStart {

    public static void main(String[] args) {

        //1.kafka鐨勯厤缃俊鎭?
        Properties prop = new Properties();
        //閾炬帴鍦板潃
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.200.130:9092");
        //key鍜寁alue鐨勫弽搴忓垪鍖栧櫒
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        //璁剧疆娑堣垂鑰呯粍
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group2");

        //鎵嬪姩鎻愪氦鍋忕Щ閲?
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);


        //2.鍒涘缓娑堣垂鑰呭璞?
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);

        //3.璁㈤槄涓婚
        consumer.subscribe(Collections.singletonList("itcast-topic-out"));

        //4.鎷夊彇娑堟伅

        //鍚屾鎻愪氦鍜屽紓姝ユ彁浜ゅ亸绉婚噺
        try {
            while (true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                    System.out.println(consumerRecord.key());
                    System.out.println(consumerRecord.value());
                    System.out.println(consumerRecord.offset());
                    System.out.println(consumerRecord.partition());
                }
                //寮傛鎻愪氦鍋忕Щ閲?
                consumer.commitAsync();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("璁板綍閿欒鐨勪俊鎭細"+e);
        }finally {
            //鍚屾
            consumer.commitSync();
        }


        /*while (true){
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
                System.out.println(consumerRecord.key());
                System.out.println(consumerRecord.value());
                System.out.println(consumerRecord.offset());
                System.out.println(consumerRecord.partition());

               *//* try {
                    //鍚屾鎻愪氦鍋忕Щ閲?
                    consumer.commitSync();
                }catch (CommitFailedException e){
                    System.out.println("璁板綍鎻愪氦澶辫触鐨勫紓甯革細"+e);
                }*//*
            }
            //寮傛鐨勬柟寮忔彁浜ゅ亸绉婚噺
            *//*consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                    if(e != null){
                        System.out.println("璁板綍閿欒鐨勬彁浜ゅ亸绉婚噺锛?+map+",寮傚父淇℃伅涓猴細"+e);
                    }
                }
            });*//*



        }*/

    }

}

