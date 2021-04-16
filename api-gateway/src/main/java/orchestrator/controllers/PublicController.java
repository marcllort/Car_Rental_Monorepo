package orchestrator.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.auth.FirebaseAuthException;
import okhttp3.RequestBody;
import okhttp3.*;
import orchestrator.handler.calendar.RetrieveCalendarHandler;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.handler.calendar.model.CalendarHandlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("public")
public class PublicController {

    @Autowired
    private RetrieveCalendarHandler calendarHandler;

    @GetMapping("data")
    public String getPublicData() {
        return "You have accessed public data from spring boot";
    }

    @PostMapping("calendar")
    public String getProtectedCalendar(@org.springframework.web.bind.annotation.RequestBody CalendarHandlerRequest request)
            throws JsonProcessingException, FirebaseAuthException {
        if (request.getFlow().equals("newService")) {
            CalendarHandlerResponse response = (CalendarHandlerResponse) calendarHandler.handle(request);

            return response.getText();
        } else {
            return "Not allowed. You must log in.";
        }
    }

    @GetMapping("covid")
    public String getProtectedCovidData(@RequestParam String origin, @RequestParam String destination) {
        return covidCall(origin, destination);
    }

    private String covidCall(String origin, String destination) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"destination\": " + destination + ",\r\n    \"origin\": " + origin + "\r\n}");
            Request request = new Request.Builder()
                    .url("https://canitravelto.wtf/travel")
                    .method("POST", body)
                    .addHeader("X-Auth-Token", "SUPER_SECRET_API_KEY")
                    .addHeader("Content-Type", "text/plain")
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
