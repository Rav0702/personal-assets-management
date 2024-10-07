package allcount.poc.credential.models;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Model representing a registration response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponseModel {
    private UUID userId;
}
