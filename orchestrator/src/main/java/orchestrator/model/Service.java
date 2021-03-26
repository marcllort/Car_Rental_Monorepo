package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Service {
    @JsonProperty("serviceId")
    private int ServiceId;
    @JsonProperty("origin")
    private String Origin;
    @JsonProperty("destination")
    private String Destination;
    @JsonProperty("clientId")
    private int ClientId;
    @JsonProperty("driverId")
    private int DriverId;
    @JsonProperty("description")
    private String Description;
    @JsonProperty("serviceDatetime")
    private ZonedDateTime ServiceDatetime;
    @JsonProperty("calendarEvent")
    private String CalendarEvent;
    @JsonProperty("payedDatetime")
    private ZonedDateTime PayedDatetime;
    @JsonProperty("basePrice")
    private Float BasePrice;
    @JsonProperty("extraPrice")
    private Float ExtraPrice;
    @JsonProperty("confirmedDatetime")
    private ZonedDateTime ConfirmedDatetime;
    @JsonProperty("passengers")
    private int Passengers;
    @JsonProperty("specialNeeds")
    private String SpecialNeeds;
}
