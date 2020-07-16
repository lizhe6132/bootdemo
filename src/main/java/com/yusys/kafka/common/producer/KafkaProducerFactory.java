package com.yusys.kafka.common.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * kafka客户端工厂
 * kafka生产者客户端是线程安全的，可以使用单例
 */
@Component
public class KafkaProducerFactory implements ApplicationListener<ContextRefreshedEvent> {
    private static Producer<Object, Object> producer;
    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducerFactory.class);
    private static final String KAFKA_PRODUCER_CONFIG = "kafka.producer.properties";
    private static final Properties properties = new Properties();
    private KafkaProducerFactory(){}
    public static Producer<Object, Object> getInstance() {
        return producer;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        LOG.info("spring容器加载完成,初始化kafka.Producer客户端...");
        init();
    }
    /**
     * 初始化kafka客户端
     */
    void init() {
        Resource resource = new ClassPathResource(KAFKA_PRODUCER_CONFIG);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            KafkaProducerFactory.properties.load(inputStream);
            LOG.info("加载kafka.producer.properties配置文件完成,bootstrap.servers:{}", properties.get("bootstrap.servers"));
            // 初始化kafka.Producer客户端
            InitProducer.createKafkaProducer();
        } catch (IOException e) {
            LOG.error("加载kafka.producer.properties配置文件失败：{}", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error("关闭kafka.producer.properties文件流失败：{}", e);
                }
            }
        }
    }
    //容器关闭时断开与kafka的连接,释放资源
    @PreDestroy
    public void destroy() {
        if (KafkaProducerFactory.producer != null) {
            KafkaProducerFactory.producer.close();
            KafkaProducerFactory.properties.clear();
            LOG.info("服务器关闭,与kafka服务端断开连接");
            KafkaProducerFactory.producer = null;
        }
    }
    static class InitProducer {
        static void createKafkaProducer() {
            if (producer == null) {
                producer =  new KafkaProducer<Object, Object>(KafkaProducerFactory.properties);
            }
        }
    }
}
