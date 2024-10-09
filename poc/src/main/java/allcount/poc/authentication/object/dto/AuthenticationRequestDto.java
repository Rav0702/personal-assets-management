package allcount.poc.authentication.object.dto;

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class AuthenticationRequestDto {
    private String username;
    private String password;
}