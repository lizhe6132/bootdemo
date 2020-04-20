package com.example.bootdemo.mq;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TestConsumerCommitSpecial {
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
        // 记录分区的偏移量
        Map<TopicPartition, OffsetAndMetadata> currOffsets = new HashMap<>(4);
        int count = 0;
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
                    currOffsets.put(new TopicPartition(record.topic(), record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "no meta"));
                    //do my work(业务异常，可能进行重试  偏移量，写入主题 异常主题)
                    //打包任务投入线程池
                    if(count%11==0){
                        //TODO 这里特定提交（异步方式，加入偏移量），每11条提交一次
                        consumer.commitAsync(currOffsets,null);
                    }
                    count++;
                }
            }
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
