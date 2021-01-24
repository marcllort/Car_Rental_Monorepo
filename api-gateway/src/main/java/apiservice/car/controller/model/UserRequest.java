package apiservice.car.controller.model;

import lombok.Data;

@Data
public class UserRequest {
    private String displayName;
    private String email;
    private String password;
    private String customClaim; // Role

    private String photoURL;
    private String disabled;
    private String emailVerified;
    private String phoneNumber;
}