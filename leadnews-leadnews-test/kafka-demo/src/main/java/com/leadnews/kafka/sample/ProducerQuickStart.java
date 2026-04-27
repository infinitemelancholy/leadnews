package com.leadnews.kafka.sample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * 鐢熶骇鑰?
 */
public class ProducerQuickStart {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //1.kafka閾炬帴閰嶇疆淇℃伅
        Properties prop = new Properties();
        //kafka閾炬帴鍦板潃
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.200.130:9092");
        //key鍜寁alue鐨勫簭鍒楀寲
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");

        //ack閰嶇疆  娑堟伅纭鏈哄埗
        prop.put(ProducerConfig.ACKS_CONFIG,"all");

        //閲嶈瘯娆℃暟
        prop.put(ProducerConfig.RETRIES_CONFIG,10);

        //鏁版嵁鍘嬬缉
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG,"lz4");

        //2.鍒涘缓kafka鐢熶骇鑰呭璞?
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(prop);

        //3.鍙戦€佹秷鎭?
        /**
         * 绗竴涓弬鏁?锛歵opic
         * 绗簩涓弬鏁帮細娑堟伅鐨刱ey
         * 绗笁涓弬鏁帮細娑堟伅鐨剉alue
         */
        for (int i = 0; i < 10; i++) {
            ProducerRecord<String,String> kvProducerRecord = new ProducerRecord<String,String>("itcast-topic-input", "hello kafka");
            producer.send(kvProducerRecord);
        }

        //鍚屾鍙戦€佹秷鎭?
        /*RecordMetadata recordMetadata = producer.send(kvProducerRecord).get();
        System.out.println(recordMetadata.offset());

        //寮傛娑堟伅鍙戦€?
        /*producer.send(kvProducerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                if(e != null){
                    System.out.println("璁板綍寮傚父淇℃伅鍒版棩蹇楄〃涓?);
                }
                System.out.println(recordMetadata.offset());
            }
        });*/

        //4.鍏抽棴娑堟伅閫氶亾  蹇呴』瑕佸叧闂紝鍚﹀垯娑堟伅鍙戦€佷笉鎴愬姛
        producer.close();



    }

}

