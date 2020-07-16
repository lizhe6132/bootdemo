package com.yusys.kafka.common.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @Since jdk1.8
 */
public interface ProducerListener {
    default void onSuccess(ProducerRecord<Object, Object> producerRecord, RecordMetadata recordMetadata) {
        this.onSuccess(producerRecord.topic(), producerRecord.partition(), producerRecord.key(), producerRecord.value(), recordMetadata);
    }

    default void onSuccess(String topic, Integer partition, Object key, Object value, RecordMetadata recordMetadata) {
    }

    default void onError(ProducerRecord<Object, Object> producerRecord, Exception exception) {
        this.onError(producerRecord.topic(), producerRecord.partition(), producerRecord.key(), producerRecord.value(), exception);
    }

    default void onError(String topic, Integer partition, Object key, Object value, Exception exception) {
    }

}
