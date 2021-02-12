package apiservice.car.handler.firebase;

import apiservice.car.model.FirebaseUserRequest;
import apiservice.car.security.EncryptionUtility;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseUserHandler {

    @Autowired
    private Firestore db;

    @Autowired
    private EncryptionUtility encryptionUtility;

    @Value("${SECRET_HASH}") // value after ':' is the default
    private String key; // 128 bit key

    public String createUserFirebaseToken(FirebaseUserRequest request) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("users").document(request.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return "User already existed. No changes were applied.";
        } else {
            // Add document data  with id of the request using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", request.getAccessToken());
            data.put("refreshToken", request.getRefreshToken());

            docRef.set(data);

            return "User succesfully created";
        }
    }

    public String createUserFirebase(FirebaseUserRequest request) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("users").document(request.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", request.getAccessToken());
            data.put("refreshToken", request.getRefreshToken());

            docRef.update(data);
            return "User already existed. No changes were applied.";
        } else {
            // Add document data  with id of the request using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", request.getAccessToken());
            data.put("refreshToken", request.getRefreshToken());

            docRef.set(data);

            return "User succesfully created";
        }
    }

    public String updateUserFirebase(FirebaseUserRequest request) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("users").document(request.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> data = new HashMap<>();
            if (!request.getName().isEmpty()) {
                data.put("name", request.getName());
            }
            if (!request.getEmail().isEmpty()) {
                data.put("email", request.getEmail());
            }
            if (!request.getPassword().isEmpty()) {
                data.put("password", encryptionUtility.encrypt(key, request.getPassword()));
            }
            if (!request.getLanguage().isEmpty()) {
                data.put("language", request.getLanguage());
            }
            if (!request.getEmailSign().isEmpty()) {
                data.put("emailSign", request.getEmailSign());
            }
            if (!request.getCalendarURL().isEmpty()) {
                data.put("calendarURL", request.getCalendarURL());
            }
            if (!request.getCity().isEmpty()) {
                data.put("city", request.getCity());
            }
            if (!request.getCountry().isEmpty()) {
                data.put("country", request.getCountry());
            }
            if (!request.getPhone().isEmpty()) {
                data.put("phone", request.getPhone());
            }
            data.put("checked", request.isChecked());

            docRef.update(data);

            return "User correctly updated!";

        } else {
            return "Error: User does not exist";
        }
    }

}
