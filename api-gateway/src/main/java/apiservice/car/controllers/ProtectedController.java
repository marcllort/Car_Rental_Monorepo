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
import apiservice.car.security.SecurityService;
import com.google.firebase.auth.FirebaseAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @Autowired
    FirebaseAuth firebaseAuth;

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


