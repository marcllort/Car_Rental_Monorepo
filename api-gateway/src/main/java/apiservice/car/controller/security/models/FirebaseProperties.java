package apiservice.car.controller.security.models;

import lombok.Data;

@Data
public class FirebaseProperties {

    private int sessionExpiryInDays;
    private String databaseUrl;
    private boolean enableStrictServerSession;
    private boolean enableCheckSessionRevoked;
    private boolean enableLogoutEverywhere;

}
