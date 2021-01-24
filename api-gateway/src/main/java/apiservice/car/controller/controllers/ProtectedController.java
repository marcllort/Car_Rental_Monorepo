package apiservice.car.controller.controllers;


import apiservice.car.controller.security.SecurityService;
import apiservice.car.controller.handler.calendar.CalendarHandlerRequest;
import apiservice.car.controller.handler.calendar.CalendarHandlerResponse;
import apiservice.car.controller.handler.calendar.RetrieveCalendarHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
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
    private RetrieveCalendarHandler handler;

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
        calendarHandlerRequest.setText("test");
        CalendarHandlerResponse response = (CalendarHandlerResponse) handler.handle(calendarHandlerRequest);

        return response.getText();
    }

    @GetMapping("email")
    public String getProtectedEmail() {

        JsonArray array = generateExample();

        return array.toString();
    }

    @GetMapping("legal")
    public String getProtectedLegal() {

        JsonArray array = generateExample();

        return array.toString();
    }

    @NotNull
    private JsonArray generateExample() {
        JsonArray array = new JsonArray();
        JsonObject item = new JsonObject();
        JsonObject availableDrivers = new JsonObject();
        availableDrivers.addProperty("me", "Me");
        availableDrivers.addProperty("chop", "Chop");
        availableDrivers.addProperty("lua", "Lua");
        item.addProperty("title", "All Day Event");
        item.addProperty("description", "Christmas eve, hohoho!");
        item.addProperty("collision", "event21");
        item.add("availableDrivers", availableDrivers);
        item.addProperty("start", "2020-12-01 12:15");
        item.addProperty("end", "2020-12-01 14:30");
        item.addProperty("backgroundColor", "#a20606");
        item.addProperty("borderColor", "#a20606");
        array.add(item);
        return array;
    }

}

