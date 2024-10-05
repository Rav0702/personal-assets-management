package allcount.poc.credential.models;

import lombok.Data;

/**
 * Model representing an authentication request.
 */
@Data
public class AuthenticationRequestModel {
    private String username;
    private String password;
}