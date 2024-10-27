package allcount.poc.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, KafkaSyncMessageDto> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, KafkaSyncMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, KafkaSyncMessageDto message) {
        kafkaTemplate.send(topic, message);
    }
}