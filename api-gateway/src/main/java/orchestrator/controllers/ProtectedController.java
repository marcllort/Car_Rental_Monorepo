package orchestrator.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import orchestrator.handler.calendar.RetrieveCalendarHandler;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.handler.calendar.model.CalendarHandlerResponse;
import orchestrator.handler.email.RetrieveEmailHandler;
import orchestrator.handler.email.model.EmailHandlerRequest;
import orchestrator.handler.email.model.EmailHandlerResponse;
import orchestrator.handler.firebase.FirebaseUserHandler;
import orchestrator.handler.legal.RetrieveLegalHandler;
import orchestrator.model.FirebaseUserRequest;
import orchestrator.model.Service;
import orchestrator.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RetrieveCalendarHandler calendarHandler;

    @Autowired
    private RetrieveLegalHandler legalHandler;

    @Autowired
    private RetrieveEmailHandler emailHandler;

    @Autowired
    private FirebaseUserHandler firebaseHandler;

    @Autowired
    private FirebaseAuth firebaseAuth;

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
        return firebaseHandler.createUserFirebase(request);
    }

    @PostMapping("update-user-firebase")
    public String updateUserFirebase(@RequestBody FirebaseUserRequest request) throws ExecutionException, InterruptedException {
        return firebaseHandler.updateUserFirebase(request);
    }

    @GetMapping("calendar")
    public String getProtectedCalendar(@RequestHeader("Authorization") String authHeader, @RequestBody CalendarHandlerRequest request)
            throws JsonProcessingException, FirebaseAuthException {
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(getIdToken(authHeader));
        request.setUserId(decodedToken.getUid());

        CalendarHandlerResponse response = (CalendarHandlerResponse) calendarHandler.handle(request);

        return response.getText();
    }

    @GetMapping("email")
    public String getProtectedEmail(@RequestHeader("Authorization") String authHeader, @RequestBody EmailHandlerRequest request)
            throws JsonProcessingException, FirebaseAuthException {
        FirebaseToken decodedToken = firebaseAuth.verifyIdToken(getIdToken(authHeader));
        request.setUserId(decodedToken.getUid());

        EmailHandlerResponse response = (EmailHandlerResponse) emailHandler.handle(request);

        response.setText("Disabled endpoint. Now it works through a cronjob. Forcing cronjob execution...");
        return response.getText();
    }

    private String getIdToken(String idToken) {
        String[] arr = idToken.split(" ", 2);
        idToken = arr[1];
        return idToken;
    }

    private CalendarHandlerRequest generateMockCalendarRequest() {
        CalendarHandlerRequest calendarHandlerRequest = new CalendarHandlerRequest();
        ZonedDateTime zdt = ZonedDateTime.of(2021, 2, 20, 0, 0, 0, 0, ZoneId.of("UTC"));
        Service service = new Service();
        service.setServiceId(4);
        service.setOrigin("BCN Airport");
        service.setDestination("Girona Airport");
        service.setClientId(1);
        service.setDriverId(1);
        service.setDescription("Test description");
        service.setServiceDatetime(zdt);
        service.setCalendarEvent("calendarURL");
        service.setPayedDatetime(zdt.plusDays(5));
        service.setBasePrice(12F);
        service.setExtraPrice((float) 0);
        //service.setConfirmedDatetime(zdt.plusDays(2));
        service.setPassengers(3);
        service.setSpecialNeeds("none");

        calendarHandlerRequest.setFlow("confirmService");
        calendarHandlerRequest.setService(service);

        return calendarHandlerRequest;
    }

    private CalendarHandlerRequest generateMockEmailRequest() {
        CalendarHandlerRequest calendarHandlerRequest = new CalendarHandlerRequest();
        ZonedDateTime zdt = ZonedDateTime.of(2021, 2, 20, 0, 0, 0, 0, ZoneId.of("UTC"));
        Service service = new Service();
        service.setServiceId(4);
        service.setOrigin("BCN Airport");
        service.setDestination("Girona Airport");
        service.setClientId(1);
        service.setDriverId(1);
        service.setDescription("Test description");
        service.setServiceDatetime(zdt);
        service.setCalendarEvent("calendarURL");
        service.setPayedDatetime(zdt.plusDays(5));
        service.setBasePrice(12F);
        service.setExtraPrice((float) 0);
        //service.setConfirmedDatetime(zdt.plusDays(2));
        service.setPassengers(3);
        service.setSpecialNeeds("none");

        calendarHandlerRequest.setService(service);

        return calendarHandlerRequest;
    }

}


