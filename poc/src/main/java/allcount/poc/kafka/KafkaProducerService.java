package allcount.poc.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka producer service.
 */
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, KafkaSyncMessageDto> kafkaTemplate;

    /**
     * Constructor.
     *
     * @param kafkaTemplate the kafka template
     */
    public KafkaProducerService(KafkaTemplate<String, KafkaSyncMessageDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Send message to the Kafka topic.
     *
     * @param topic the topic
     * @param message the message
     */
    public void sendMessage(String topic, KafkaSyncMessageDto message) {
        kafkaTemplate.send(topic, message);
    }
}