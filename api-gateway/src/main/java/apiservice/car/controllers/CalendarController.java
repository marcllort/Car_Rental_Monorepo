package apiservice.car.controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Events;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("public")
public class CalendarController {

    private static final String APPLICATION_NAME = "Car Rental";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static HttpTransport httpTransport;

    final DateTime date1 = new DateTime("2017-05-05T16:30:00.000+05:30");
    final DateTime date2 = new DateTime(new Date());
    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow flow;
    Credential credential;

    @Autowired
    private Firestore db;

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Value("${google.client.client-id}")
    private String clientId;

    @Value("${google.client.client-secret}")
    private String clientSecret;

    @Value("${google.client.redirectUri}")
    private String redirectURI;

    @GetMapping(value = "login")
    public ResponseEntity<String> getCalendarEvents(@RequestHeader("Authorization") String authHeader) throws Exception {
        com.google.api.services.calendar.model.Events eventList;
        String message = "";
        String idToken = getIdToken(authHeader);

        flowSetup();

        if (isFirstTime(idToken)) {
            GoogleTokenResponse token = getNewToken(idToken);
            eventList = getEvents(token);
            message = eventList.getItems().toString();
        } else {
            TokenResponse token = getTokenWithRefreshToken(idToken);
            eventList = getEvents(token);
            message = eventList.getItems().toString();
        }

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    private String getIdToken(String idToken) {
        String[] arr = idToken.split(" ", 2);
        idToken = arr[1];
        return idToken;
    }

    private void flowSetup() throws Exception {
        if (flow == null) {
            GoogleClientSecrets.Details web = new GoogleClientSecrets.Details();
            web.setClientId(clientId);
            web.setClientSecret(clientSecret);
            clientSecrets = new GoogleClientSecrets().setWeb(web);
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                    Collections.singleton(CalendarScopes.CALENDAR)).setAccessType("offline").setApprovalPrompt("consent").build();
        }
        flow.newAuthorizationUrl().setRedirectUri(redirectURI);
    }

    private boolean isFirstTime(String idToken) throws Exception {
        FirebaseToken uid = firebaseAuth.verifyIdToken(idToken);
        DocumentReference docRef = db.collection("users").document(uid.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            if (document.getData().get("refreshToken") != null) {
                return false;
            } else return document.getData().get("code") != null;
        } else {
            throw new Exception("No such document!");
        }
    }

    private TokenResponse getTokenWithRefreshToken(String idToken) throws Exception {
        ArrayList<String> scopes = new ArrayList<>();
        scopes.add(CalendarScopes.CALENDAR);

        String refreshToken = getRefreshToken(idToken);

        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(httpTransport, JSON_FACTORY,
                refreshToken, clientId, clientSecret).setScopes(scopes).setGrantType("refresh_token").execute();

        return tokenResponse;
    }

    private Events getEvents(TokenResponse token) throws IOException {
        Events eventList;
        credential = flow.createAndStoreCredential(token, "userID");
        Calendar client = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

        Calendar.Events events = client.events();
        eventList = events.list("primary").setTimeMin(date1).setTimeMax(date2).execute();
        return eventList;
    }

    private String getRefreshToken(String idToken) throws Exception {
        String refreshToken = null;

        FirebaseToken uid = firebaseAuth.verifyIdToken(idToken);
        DocumentReference docRef = db.collection("users").document(uid.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            refreshToken = String.valueOf(document.getData().get("refreshToken"));
        } else {
            throw new Exception("No such document!");
        }

        return refreshToken;
    }

    private String getCodeToken(String idToken) throws Exception {
        String code = null;

        FirebaseToken uid = firebaseAuth.verifyIdToken(idToken);
        DocumentReference docRef = db.collection("users").document(uid.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            code = String.valueOf(document.getData().get("code"));
        } else {
            throw new Exception("No such document!");
        }

        return code;
    }

    private GoogleTokenResponse getNewToken(String idToken) throws Exception {
        String code = getCodeToken(idToken);

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(httpTransport,
                JSON_FACTORY, "https://oauth2.googleapis.com/token", clientId,
                clientSecret, code, redirectURI).execute();

        updateRefreshToken(idToken, tokenResponse);

        return tokenResponse;
    }

    private void updateRefreshToken(String idToken, GoogleTokenResponse tokenResponse) throws FirebaseAuthException, InterruptedException, ExecutionException {
        FirebaseToken uid = firebaseAuth.verifyIdToken(idToken);
        DocumentReference docRef = db.collection("users").document(uid.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> data = new HashMap<>();
            data.put("refreshToken", tokenResponse.getRefreshToken());
            data.put("code", FieldValue.delete());
            docRef.update(data);
        } else {
            // Add document data  with id of the request using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("refreshToken", tokenResponse.getRefreshToken());

            docRef.set(data);
        }
    }
}
