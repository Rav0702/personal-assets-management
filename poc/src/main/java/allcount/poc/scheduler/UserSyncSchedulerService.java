package allcount.poc.scheduler;

import allcount.poc.kafka.KafkaProducerService;
import allcount.poc.kafka.KafkaSyncMessageDto;
import allcount.poc.user.repository.AllcountUserRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service for syncing users.
 */
@Service
public class UserSyncSchedulerService {
    private static Logger LOG = LoggerFactory.getLogger(UserSyncSchedulerService.class);

    private final AllcountUserRepository allcountUserRepository;
    private final KafkaProducerService kafkaProducerService;
    private static final String TOPIC = "sync-job";

    /**
     * Constructor.
     *
     * @param allcountUserRepository the AllcountUserRepository
     * @param kafkaProducerService   the KafkaProducerService
     */
    @Autowired
    public UserSyncSchedulerService(AllcountUserRepository allcountUserRepository,
                                    KafkaProducerService kafkaProducerService) {
        this.allcountUserRepository = allcountUserRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Schedule user sync.
     */
    @Scheduled(fixedRate = 60000)
    public void scheduleUserSync() {
        LOG.info("Starting user sync job");
        List<UUID> userIds = allcountUserRepository.findAllUserIds();

        for (UUID userId : userIds) {
            KafkaSyncMessageDto syncMessageDto = new KafkaSyncMessageDto(userId, true, true, null, null);
            kafkaProducerService.sendMessage(TOPIC, syncMessageDto);
        }
    }
}