package com.coviscon.postservice.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

//@EnableKafka
//@Configuration
public class KafkaConsumerConfig {

    /**
     * Kafka 접속을 위한 설정 정보 set
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        // 설정 정보 저장
        Map<String, Object> properties = new HashMap<>();

        // kafka ip set
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");

        // consumer group set
        // 뒤에 문자열은 임의로 주는 것으로 변경 가능함
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroupId");

        // topic에 저장되어 있는 key, value 값을 가져와 Deserialize
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    /**
     * topic 변경 사항을 감지하는 event Listener
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        // topic에 변경사항이 생겼을 때 이를 캐치래 DB에 반영하는 등의 용도
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory
            = new ConcurrentKafkaListenerContainerFactory<>();

        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory());

        return kafkaListenerContainerFactory;
    }
}
