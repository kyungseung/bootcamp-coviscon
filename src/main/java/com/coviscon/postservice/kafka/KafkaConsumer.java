package com.coviscon.postservice.kafka;

import com.coviscon.postservice.repository.CommentRepository;
import com.coviscon.postservice.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
//@Service
public class KafkaConsumer {

    private final PostRepository postRepository;
//    private final CommentRepository commentRepository;

    // 가져오고 싶은 정보의 이름을 기반으로 작성 item-topic
    @KafkaListener(topics = "item-post-topic")
    public void update(String kafkaMessage) {
        log.info("[KafkaConsumer] kafkaMessage : {}", kafkaMessage);

        // Deserialize set
        ObjectMapper mapper = new ObjectMapper();
        Map<Object, Object> map = new HashMap<>();

        // Deserialize (역직렬화)
        try {
            // producer 에서 보낸 String type JSON data를 map에 담음
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        postRepository.findById((Long) map.get("itemId")).orElseThrow(IllegalAccessError::new);
    }
}
