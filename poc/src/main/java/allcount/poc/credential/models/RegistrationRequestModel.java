package allcount.poc.credential.models;

import lombok.Data;


/**
 * Model representing a registration request.
 */

@Data
public class RegistrationRequestModel {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}