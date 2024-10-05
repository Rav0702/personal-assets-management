package allcount.poc.core.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Base class for all entities in the POC application.
 */
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AllcountEntity {
    protected static final String ERROR_ENTITY_HASH_MISMATCH = "Entity hash mismatch, please investigate.";
    protected static final String ALGORITHM_SHA_256 = "SHA-256";

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(updatable = false, nullable = false, columnDefinition = "char(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    protected UUID id;

    @CreatedDate
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(nullable = false)
    protected LocalDateTime updatedTimestamp;

    @ToString.Exclude
    @Column(columnDefinition = "BINARY(32)", nullable = false)
    protected byte[] entityHash;

    /**
     * Updates the entity hash before persisting the entity.
     *
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    @PrePersist()
    protected void updateEntityHash() throws NoSuchAlgorithmException {
        this.entityHash = this.calculateEntityHash();
    }

    /**
     * Verifies the entity hash after loading the entity.
     *
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    @PostLoad()
    protected void verifyEntityHash() throws NoSuchAlgorithmException {
        if (!Arrays.equals(this.entityHash, this.calculateEntityHash())) {
            throw new RuntimeException(ERROR_ENTITY_HASH_MISMATCH);
        }
    }

    /**
     * Returns a string representation of the entity for hashing purposes only.
     *
     * @return a string representation of the entity for hashing purposes only
     */
    protected abstract String toStringForHashOnly();

    /**
     * Calculates the hash of the entity.
     *
     * @return the hash of the entity
     * @throws NoSuchAlgorithmException if the SHA-256 algorithm is not available
     */
    protected byte[] calculateEntityHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM_SHA_256);

        return digest.digest(this.toStringForHashOnly().getBytes(StandardCharsets.UTF_8));
    }
}
