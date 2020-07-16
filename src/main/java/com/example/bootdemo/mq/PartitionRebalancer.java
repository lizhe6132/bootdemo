package com.example.bootdemo.mq;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分区再均衡监听器
 */
public class PartitionRebalancer implements ConsumerRebalanceListener {
    private static final Logger LOG = LoggerFactory.getLogger(PartitionRebalancer.class);
    // 模拟偏移量入库
    public final ConcurrentHashMap<TopicPartition, Long> concurrentHashMap = new ConcurrentHashMap<>();
    // 分区偏移量(加入此监听器的消费者记录的分区偏移量)
    private final Map<TopicPartition, OffsetAndMetadata> currOffsetMap;
    // 加入此监听器的分区的消费者
    private final Consumer<String, String> consumer;
    public PartitionRebalancer(Map<TopicPartition, OffsetAndMetadata> currOffsetMap,
                               Consumer<String, String> consumer) {
        this.currOffsetMap = currOffsetMap;
        this.consumer = consumer;

    }
    /**
     * 分区再均衡前，记录各分区偏移量入库
     * @param collection
     */
    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
        LOG.info("触发分区再均衡，当前偏移量:{}", currOffsetMap);
        for (TopicPartition partition: collection) {
            OffsetAndMetadata metadata = currOffsetMap.get(partition);
            if (metadata == null) {
                continue;
            }
            concurrentHashMap.put(partition, metadata.offset());
        }
        // 最后再确保一次偏移量提交(当偏移量入库失败，尽最大努力使分区保持最新的偏移量)
        consumer.commitSync(currOffsetMap);
    }

    /**
     * 分区在均衡完成,设置个分区偏移量
     * @param collection
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        System.out.println("-再均衡完成，onPartitionsAssigned参数值为："+collection);
        System.out.println("分区偏移量表中："+concurrentHashMap);
        if (concurrentHashMap.size() > 0) {
            for (TopicPartition partition: collection) {
                Long currOffset = concurrentHashMap.get(partition);
                if (currOffset == null) {
                    continue;
                }
                consumer.seek(partition, currOffset);
            }
        }

    }
    public ConcurrentHashMap<TopicPartition, Long> getConcurrentHashMap() {
        return concurrentHashMap;
    }
}
