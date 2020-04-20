package com.example.bootdemo.mq;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class TestProducer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //TODO 生产者三个属性必须指定(broker地址清单、key和value的序列化器)
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<String, String>(properties);
        try {
            ProducerRecord<String,String> record;
            try {
                //TODO 发送4条消息
                for(int i=0;i<4;i++){
                    record = new ProducerRecord<String,String>("user-behaviors", "1001","lison");
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
