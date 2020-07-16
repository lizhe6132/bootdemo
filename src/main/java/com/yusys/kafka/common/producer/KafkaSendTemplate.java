package com.yusys.kafka.common.producer;

import com.yusys.kafka.common.SpringContextHolder;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * 模板模式+观察者模式
 */
@Component
public class KafkaSendTemplate {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaSendTemplate.class);

    /**
     * 发送数据模板方法
     * @param topic
     * @param partition
     * @param key
     * @param data
     */
    public Future<RecordMetadata> sendData(String topic, Integer partition, Object key, Object data) {
        if (null == topic || "".equals(topic)) {
            LOG.info("主题topic不能为空,终止发送");
            return null;
        }
        if (null == data) {
            LOG.info("数据data不能为空,终止发送");
            return null;
        }
        Producer<Object, Object> producer = KafkaProducerFactory.getInstance();
        final ProducerRecord<Object,Object> producerRecord = new ProducerRecord<Object,Object>(topic, partition,key, data);
        Future<RecordMetadata> future =  producer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception e) {
                // 使用者提供监听器
                ProducerListener producerListener = SpringContextHolder.getBean(ProducerListener.class);
                if (null == e) {
                    if (producerListener != null) {
                        producerListener.onSuccess(producerRecord, metadata);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("发送成功: " + producerRecord + ", metadata: " + metadata);
                    }
                } else {
                    if (producerListener != null) {
                        producerListener.onError(producerRecord, e);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("发送失败 : " + producerRecord , metadata);
                    }
                }

            }
        });
        return future;
    }
}
