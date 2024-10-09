package allcount.poc.authentication.object.dto;

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
public class RegistrationResponseDto {
    private UUID userId;
}
