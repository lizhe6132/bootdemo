package com.example.bootdemo.mq;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * 消费者加入分区再均衡监听器并且偏移量入库
 */
public class ConsumerWithRebalance {

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

        // 记录分区偏移量
        final Map<TopicPartition, OffsetAndMetadata> currOffsetMap = new HashMap<>();
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);
        PartitionRebalancer rebalancer = new PartitionRebalancer(currOffsetMap, consumer);
        try {
            //TODO 消费者订阅主题（可以多个）,加入分区再均衡监听器
            consumer.subscribe(Collections.singletonList("user-behaviors"), rebalancer);
            while(true){
                //TODO 拉取（新版本）
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
                for(ConsumerRecord<String, String> record:records){
                    System.out.println(String.format("topic:%s,分区：%d,偏移量：%d," + "key:%s,value:%s",record.topic(),record.partition(),
                            record.offset(),record.key(),record.value()));
                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                    currOffsetMap.put(partition,
                            new OffsetAndMetadata(record.offset() + 1, "no meta"));
                    //do my work(业务异常，可能进行重试  偏移量，写入主题 异常主题)
                    //打包任务投入线程池
                }
                // 循环结束，异步提交偏移量
                consumer.commitAsync(currOffsetMap,null);
                // 模拟提交偏移量入库
                commitOffset(currOffsetMap, rebalancer);
            }
        } catch (CommitFailedException e) {
            System.out.println("commit field");
        }finally {
            try {
                commitOffset(currOffsetMap, rebalancer);

                //TODO 为了万不一失，需要同步提交下
                consumer.commitSync(currOffsetMap);
            } finally {
                consumer.close();
            }
        }
    }

    private static void commitOffset(Map<TopicPartition, OffsetAndMetadata> currOffsetMap, PartitionRebalancer rebalancer) {
        // 偏移量入库
        Iterator<Map.Entry<TopicPartition, OffsetAndMetadata>> iterator = currOffsetMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<TopicPartition, OffsetAndMetadata> entry = iterator.next();
            TopicPartition topicPartition = entry.getKey();
            OffsetAndMetadata metadata = entry.getValue();
            // 模拟偏移量入库
            rebalancer.getConcurrentHashMap().put(topicPartition, metadata.offset());
        }
    }
}
