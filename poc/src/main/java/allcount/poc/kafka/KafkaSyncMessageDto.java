package allcount.poc.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * This DTO is used to send the message to the Kafka topic.
 */
@Setter
@Getter
@ToString
public class KafkaSyncMessageDto implements Serializable {
    private final UUID userId;
    private final boolean syncAccounts;
    private final boolean syncTransactions;
    private final LocalDate syncFrom;
    private final LocalDate syncTo;

    /**
     * Constructor.
     *
     * @param userId the user id
     * @param syncAccounts the flag to sync accounts
     * @param syncTransactions the flag to sync transactions
     * @param syncFrom the date to sync from
     * @param syncTo the date to sync to
     */
    @JsonCreator
    public KafkaSyncMessageDto(
            @JsonProperty("userId") UUID userId,
            @JsonProperty("syncAccounts") boolean syncAccounts,
            @JsonProperty("syncTransactions") boolean syncTransactions,
            @JsonProperty("syncFrom") LocalDate syncFrom,
            @JsonProperty("syncTo") LocalDate syncTo) {
        this.userId = userId;
        this.syncAccounts = syncAccounts;
        this.syncTransactions = syncTransactions;
        this.syncFrom = syncFrom;
        this.syncTo = syncTo;
    }
}
