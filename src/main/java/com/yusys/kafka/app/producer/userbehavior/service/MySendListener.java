package com.yusys.kafka.app.producer.userbehavior.service;

import com.yusys.kafka.common.producer.ProducerListener;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @since jdk1.8
 */
@Component
public class MySendListener implements ProducerListener {
    private static final Logger LOG = LoggerFactory.getLogger(MySendListener.class);
    @Override
    public void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
        LOG.error("发送失败,topic:{}, partition:{},key:{},value:{},exception:{}",
                topic, partition, key, value, exception);
    }
}
