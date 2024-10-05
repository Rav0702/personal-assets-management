package allcount.poc.credential.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Model representing a registration response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseModel {
    private UUID userId;
}
