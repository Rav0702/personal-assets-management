package allcount.poc.kafka;


import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This DTO is used to send the message to the Kafka topic.
 */
@Setter
@Getter
@AllArgsConstructor
@ToString
public class KafkaSyncMessageDto {
    private final UUID userId;
    private final boolean syncAccounts;
    private final boolean syncTransactions;
    private final LocalDate syncFrom;
    private final LocalDate syncTo;
}
