package apiservice.car.controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.core.ApiFuture;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
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
import java.security.GeneralSecurityException;
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
    private final Set<Event> events = new HashSet<>();
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
    public ResponseEntity<String> oauth2Callback(@RequestHeader("Authorization") String idToken) throws Exception {
        com.google.api.services.calendar.model.Events eventList;
        String[] arr = idToken.split(" ", 2);

        idToken = arr[1];

        String message = "";

        authorize();
        //TokenResponse token = getNewToken(idToken);


        //eventList = getEvents(token);

        TokenResponse token2 = getUserToken(idToken);
        token2.setAccessToken(refreshToken(token2.getRefreshToken()));
        eventList = getEvents(token2);
        /*try {
            TokenResponse token = getNewToken(idToken);

            eventList = getEvents(token);
            message = eventList.getItems().toString();
        } catch (Exception e) {
            GoogleJsonResponseException googleJsonResponseException = (GoogleJsonResponseException) e;
            if (googleJsonResponseException.getStatusCode() == 401) {
                TokenResponse token = getUserToken(idToken);
                token.setAccessToken(refreshToken(token.getRefreshToken()));
                eventList = getEvents(token);
                message = eventList.getItems().toString();
            } else {
                message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                        + " Redirecting to google connection status page.";
            }
        }*/

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public String refreshToken(String refreshToken) throws IOException {
        ArrayList<String> scopes = new ArrayList<>();

        scopes.add(CalendarScopes.CALENDAR);

        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(new NetHttpTransport(), new JacksonFactory(),
                refreshToken, clientId, clientSecret).setScopes(scopes).setGrantType("refresh_token").execute();

        return tokenResponse.getAccessToken();
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

    private TokenResponse getUserToken(String authHeader) throws ExecutionException, InterruptedException, FirebaseAuthException {
        TokenResponse tokenResponse = new TokenResponse();
        FirebaseToken uid = firebaseAuth.verifyIdToken(authHeader);
        DocumentReference docRef = db.collection("users").document(uid.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();

        DocumentSnapshot document = future.get();
        if (document.exists()) {
            tokenResponse.setAccessToken(String.valueOf(document.getData().get("accessToken")));
            tokenResponse.setRefreshToken(String.valueOf(document.getData().get("refreshToken")));
            tokenResponse.setScope(String.valueOf(document.getData().get("code")));
        } else {
            System.out.println("No such document!");
        }

        return tokenResponse;
    }

    private void authorize() throws Exception {
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


    public GoogleTokenResponse getNewToken(String authHeader) throws IOException, GeneralSecurityException, ExecutionException, InterruptedException, FirebaseAuthException {
        TokenResponse token = getUserToken(authHeader);
        String code = token.getScope();


        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        clientId,
                        clientSecret,
                        code,
                        "postmessage")  // Specify the same redirect URI that you use with your web
                        // app. If you don't have a web version of your app, you can
                        // specify an empty string.
                        .execute();
        FirebaseToken uid = firebaseAuth.verifyIdToken(authHeader);
        DocumentReference docRef = db.collection("users").document(uid.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", tokenResponse.getAccessToken());
            data.put("refreshToken", tokenResponse.getRefreshToken());

            docRef.update(data);
        } else {
            // Add document data  with id of the request using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", tokenResponse.getAccessToken());
            data.put("refreshToken", tokenResponse.getRefreshToken());

            docRef.set(data);
        }


        return tokenResponse;

    }
}
