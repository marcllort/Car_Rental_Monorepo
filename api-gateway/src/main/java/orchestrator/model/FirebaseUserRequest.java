package orchestrator.model;

import lombok.Data;

@Data
public class FirebaseUserRequest {

    private String uid;
    private String code;
    private String name;
    private String language;
    private String email;
    private String password;
    private String emailSign;
    private String calendarURL;


    // Optional
    private boolean checked;
    private String city;
    private String country;
    private String phone;
}
