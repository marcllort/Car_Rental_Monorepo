package apiservice.car.model;

import lombok.Data;

@Data
public class UserRequest {
    private String displayName;
    private String email;
    private String password;
    private String customClaim; // Role
}