package apiservice.car.controllers;


import apiservice.car.handler.calendar.RetrieveCalendarHandler;
import apiservice.car.handler.calendar.model.CalendarHandlerRequest;
import apiservice.car.handler.calendar.model.CalendarHandlerResponse;
import apiservice.car.handler.email.RetrieveEmailHandler;
import apiservice.car.handler.email.model.EmailHandlerRequest;
import apiservice.car.handler.email.model.EmailHandlerResponse;
import apiservice.car.handler.firebase.FirebaseUserHandler;
import apiservice.car.handler.legal.RetrieveLegalHandler;
import apiservice.car.handler.legal.model.LegalHandlerRequest;
import apiservice.car.handler.legal.model.LegalHandlerResponse;
import apiservice.car.model.FirebaseUserRequest;
import apiservice.car.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


