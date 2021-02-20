package apiservice.car.controllers;

import okhttp3.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("public")
public class PublicController {

    @GetMapping("data")
    public String getPublicData() {
        return "You have accessed public data from spring boot";
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
