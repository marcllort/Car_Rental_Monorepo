package apiservice.car.model;

import lombok.Data;

@Data
public class FirebaseUserRequest {

    private String uid;
    private String name;
    private String language;
    private String email;
    private String password;
    private String emailSign;
    private String calendarURL;

}
