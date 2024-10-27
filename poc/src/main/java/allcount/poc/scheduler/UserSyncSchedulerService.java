package allcount.poc.scheduler;

import allcount.poc.kafka.KafkaProducerService;
import allcount.poc.kafka.KafkaSyncMessageDto;
import allcount.poc.user.entity.AllcountUser;
import allcount.poc.user.repository.AllcountUserRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSyncSchedulerService {

    private final AllcountUserRepository allcountUserRepository;
    private final KafkaProducerService kafkaProducerService;
    private static final String TOPIC = "sync-job";
    private static final Integer SYNC_INTERVAL = 60000; // 1 minute in milliseconds

    @Autowired
    public UserSyncSchedulerService(AllcountUserRepository allcountUserRepository,
                                    KafkaProducerService kafkaProducerService) {
        this.allcountUserRepository = allcountUserRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Scheduled(fixedRate = 60000) // 1 minute in milliseconds
    public void scheduleUserSync() {
        System.out.println("Syncing users...");
        List<UUID> userIds = allcountUserRepository.findAllUserIds();
        for (UUID userId : userIds) {
            KafkaSyncMessageDto syncMessageDto = new KafkaSyncMessageDto(userId, true, true, null, null);
            kafkaProducerService.sendMessage(TOPIC, syncMessageDto);
        }
    }
}