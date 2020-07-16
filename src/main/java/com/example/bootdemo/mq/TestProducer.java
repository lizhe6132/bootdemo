package com.example.bootdemo.mq;

import com.yusys.kafka.common.producer.KafkaProducerFactory;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TestProducer {
    public static void main(String[] args) {
        Producer<Object, Object> producer = KafkaProducerFactory.getInstance();
        try {
            ProducerRecord<Object,Object> record;
            try {
                //TODO 发送4条消息
                for(int i=0;i<4;i++){
                    record = new ProducerRecord<Object,Object>("user-behaviors", "1002","lison");
                    producer.send(record);//发送并发忘记（重试会有）
                    System.out.println(i+"，message is sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            producer.close();
        }
    }
}
