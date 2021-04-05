package orchestrator.business;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import orchestrator.handler.calendar.model.CalendarHandlerRequest;
import orchestrator.model.Geocode;
import orchestrator.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PriceCalculator {

    @Autowired
    private ObjectMapper mapper;

    @Value("${HERE_API_KEY}")
    private String apiKey;

    private int apiCallRoute(String origin, String destination) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("https://router.hereapi.com/v8/routes?transportMode=car&origin=" + origin + "&destination="
                        + destination + "&return=summary&apiKey=5whVWVf50L_WeDlfSakfJNXlK20qcud9507K5UWpBq8")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        String bodyString = body.string();
        Route route = mapper.readValue(bodyString, Route.class);

        return route.getRoutes().get(0).sections.get(0).summary.length;
    }

    private String apiCallLocation(String location) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://geocode.search.hereapi.com/v1/geocode?q=" + location + "&apiKey=5whVWVf50L_WeDlfSakfJNXlK20qcud9507K5UWpBq8")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        Geocode geocode = null;
        try {
            geocode = mapper.readValue(response.body().string(), Geocode.class);
        } catch (Exception exception) {
            return "41.38804,2.17001";
        }

        return geocode.getItems().get(0).position.lat + "," + geocode.getItems().get(0).position.lng;
    }

    public float generatePrice(CalendarHandlerRequest request) throws IOException {
        float price = 0;

        String origin = request.getService().getOrigin();
        String originGeo = apiCallLocation(origin);
        String destination = request.getService().getDestination();
        String destinationGeo = apiCallLocation(destination);

        int distance = apiCallRoute(originGeo, destinationGeo);

        if (distance != 0) {
            price = (float) distance * 50;
        } else {
            price = 90;
        }

        return price;
    }

}
