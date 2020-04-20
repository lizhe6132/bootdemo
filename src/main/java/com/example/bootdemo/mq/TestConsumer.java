package com.example.bootdemo.mq;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class TestConsumer {
    public static void main(String[] args) {
        //TODO 消费者三个属性必须指定(broker地址清单、key和value的反序列化器)
        Properties properties = new Properties();
        properties.put("bootstrap.servers","127.0.0.1:9092");
        properties.put("key.deserializer", StringDeserializer.class);
        properties.put("value.deserializer", StringDeserializer.class);
        //TODO 群组并非完全必须
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"consumer1");
        //TODO 关闭自动提交
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        try {
            //TODO 消费者订阅主题（可以多个）
            consumer.subscribe(Collections.singletonList("user-behaviors"));
            while(true){
                //TODO 拉取（新版本）
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for(ConsumerRecord<String, String> record:records){
                    //提交偏移量(提交越频繁，性能越差)
                    System.out.println(String.format("topic:%s,分区：%d,偏移量：%d," + "key:%s,value:%s",record.topic(),record.partition(),
                            record.offset(),record.key(),record.value()));
                    //do my work(业务异常，可能进行重试  偏移量，写入主题 异常主题)
                    //打包任务投入线程池
                }
                //提交偏移量
                // 同步提交（这个方法会阻塞）
                //consumer.commitSync(); //offset =200  max
                // 异步提交回调
                consumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                        if (e != null) {
                            System.out.println("commit fail offset:" + map);
                            System.out.println(e.getMessage());
                        }
                    }
                });
            }

            //通过另外一个线程 consumer. wakeup()
        } catch (CommitFailedException e) {
            System.out.println("commit field");
        }finally {
            try {
                //TODO 为了万不一失，需要同步提交下
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}
