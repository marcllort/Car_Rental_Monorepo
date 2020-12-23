package apiservice.car.controllers;


import apiservice.car.security.SecurityService;
import com.google.firebase.auth.FirebaseAuth;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("protected")
public class ProtectedController {

    @Autowired
    FirebaseAuth firebaseAuth;
    @Autowired
    private SecurityService securityService;

    @GetMapping("data")
    public String getProtectedData() {
        String name = securityService.getUser().getName();

        if (name == null) {
            return "You have accessed protected data from spring boot";
        } else {
            return name.split("\\s+")[0] + ", you have accessed protected data from spring boot.";
        }
    }

    @GetMapping("covid")
    public String getProtectedCovidData() {
        return covidCall();
    }

    private String covidCall() {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "{\r\n    \"destination\": \"Spain\",\r\n    \"origin\": \"France\"\r\n}");
            Request request = new Request.Builder()
                    .url("https://canitravelto.wtf/travel")
                    .method("POST", body)
                    .addHeader("X-Auth-Token", "SUPER_SECRET_API_KEY")
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Cookie", "__cfduid=d92e76abef0fafb49acf4087bee8a085a1608641092")
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}


