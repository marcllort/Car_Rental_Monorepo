package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ServiceCaps {
    @JsonProperty("ServiceId")
    private int ServiceId;
    @JsonProperty("Origin")
    private String Origin;
    @JsonProperty("Destination")
    private String Destination;
    @JsonProperty("ClientId")
    private int ClientId;
    @JsonProperty("DriverId")
    private int DriverId;
    @JsonProperty("Description")
    private String Description;
    @JsonProperty("ServiceDatetime")
    private ZonedDateTime ServiceDatetime;
    @JsonProperty("CalendarEvent")
    private String CalendarEvent;
    @JsonProperty("PayedDatetime")
    private ZonedDateTime PayedDatetime;
    @JsonProperty("BasePrice")
    private Float BasePrice;
    @JsonProperty("ExtraPrice")
    private Float ExtraPrice;
    @JsonProperty("ConfirmedDatetime")
    private ZonedDateTime ConfirmedDatetime;
    @JsonProperty("Passengers")
    private int Passengers;
    @JsonProperty("SpecialNeeds")
    private String SpecialNeeds;
}
