package allcount.poc.authentication.object.dto;

import lombok.Data;


/**
 * Model representing a registration request.
 */

@Data
public class RegistrationRequestDto {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}