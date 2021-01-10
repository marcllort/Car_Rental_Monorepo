package apiservice.car.controllers;

import apiservice.car.security.CookieService;
import apiservice.car.security.SecurityService;
import apiservice.car.security.models.Credentials;
import apiservice.car.security.models.SecurityProperties;
import apiservice.car.security.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.SessionCookieOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("session")
public class SessionController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private CookieService cookieUtils;
    @Autowired
    private SecurityProperties secProps;

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public void getOption(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE");
    }

    @PostMapping("login")
    public void sessionLogin(HttpServletRequest request) {
        String idToken = securityService.getBearerToken(request);
        int sessionExpiryDays = secProps.getFirebaseProps().getSessionExpiryInDays();
        long expiresIn = TimeUnit.DAYS.toMillis(sessionExpiryDays);
        SessionCookieOptions options = SessionCookieOptions.builder().setExpiresIn(expiresIn).build();
        try {
            String sessionCookieValue = FirebaseAuth.getInstance().createSessionCookie(idToken, options);
            cookieUtils.setSecureCookie("session", sessionCookieValue, sessionExpiryDays);
            cookieUtils.setCookie("authenticated", Boolean.toString(true), sessionExpiryDays);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("logout")
    public void sessionLogout() {
        if (securityService.getCredentials().getType() == Credentials.CredentialType.SESSION
                && secProps.getFirebaseProps().isEnableLogoutEverywhere()) {
            try {
                FirebaseAuth.getInstance().revokeRefreshTokens(securityService.getUser().getUid());
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        }
        cookieUtils.deleteSecureCookie("session");
        cookieUtils.deleteCookie("authenticated");
    }

    @PostMapping("me")
    public User getUser() {
        return securityService.getUser();
    }

    @GetMapping("create/token")
    public String getCustomToken() throws FirebaseAuthException {
        return FirebaseAuth.getInstance().createCustomToken(String.valueOf(securityService.getUser().getUid()));
    }

}
