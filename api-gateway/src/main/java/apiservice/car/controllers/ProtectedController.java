package apiservice.car.controllers;


import apiservice.car.handler.calendar.RetrieveCalendarHandler;
import apiservice.car.handler.calendar.model.CalendarHandlerRequest;
import apiservice.car.handler.calendar.model.CalendarHandlerResponse;
import apiservice.car.handler.email.RetrieveEmailHandler;
import apiservice.car.handler.email.model.EmailHandlerRequest;
import apiservice.car.handler.email.model.EmailHandlerResponse;
import apiservice.car.handler.legal.RetrieveLegalHandler;
import apiservice.car.handler.legal.model.LegalHandlerRequest;
import apiservice.car.handler.legal.model.LegalHandlerResponse;
import apiservice.car.model.FirebaseUserRequest;
import apiservice.car.security.SecurityService;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @Autowired
    FirebaseAuth firebaseAuth;

    @Autowired
    Firestore db;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RetrieveCalendarHandler calendarHandler;

    @Autowired
    private RetrieveLegalHandler legalHandler;

    @Autowired
    private RetrieveEmailHandler emailHandler;

    @GetMapping("data")
    public String getProtectedData() {
        String name = securityService.getUser().getName();

        if (name == null) {
            return "You have accessed protected data from spring boot";
        } else {
            return name.split("\\s+")[0] + ", you have accessed protected data from spring boot.";
        }
    }

    @PostMapping("create-user-firebase")
    public String createUserFirebase(@RequestBody FirebaseUserRequest request) throws ExecutionException, InterruptedException {

        DocumentReference docRef = db.collection("users").document(request.getUid());
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            return "User already existed. No changes were applied.";
        } else {
            // Add document data  with id of the request using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("name", request.getName());
            data.put("email", request.getEmail());
            data.put("password", request.getPassword());
            data.put("language", request.getLanguage());
            data.put("emailSign", request.getEmailSign());
            data.put("calendarURL", request.getCalendarURL());
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);

            return "User succesfully created";
        }
    }

    @PostMapping("update-user-firebase")
    public String updateUserFirebase(@RequestBody FirebaseUserRequest request) throws ExecutionException, InterruptedException {

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
                data.put("password", request.getPassword());
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


            ApiFuture<WriteResult> future2 = docRef.update(data);
            WriteResult result = future2.get();

            return "User correctly updated!";

        } else {
            return "Error: User does not exist";
        }
    }

    @GetMapping("calendar")
    public String getProtectedCalendar() {
        CalendarHandlerRequest calendarHandlerRequest = new CalendarHandlerRequest();
        calendarHandlerRequest.setText("calendar-test");
        CalendarHandlerResponse response = (CalendarHandlerResponse) calendarHandler.handle(calendarHandlerRequest);

        return response.getText();
    }

    @GetMapping("email")
    public String getProtectedEmail() {
        EmailHandlerRequest emailHandlerRequest = new EmailHandlerRequest();
        emailHandlerRequest.setText("email-test");
        EmailHandlerResponse response = (EmailHandlerResponse) emailHandler.handle(emailHandlerRequest);

        return response.getText();
    }

    @GetMapping("legal")
    public String getProtectedLegal() {
        LegalHandlerRequest legalHandlerRequest = new LegalHandlerRequest();
        legalHandlerRequest.setText("legal-test");
        LegalHandlerResponse response = (LegalHandlerResponse) legalHandler.handle(legalHandlerRequest);

        return response.getText();
    }

}


