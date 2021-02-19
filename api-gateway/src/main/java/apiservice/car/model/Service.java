package apiservice.car.model;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Service {
    private int ServiceId;
    private String Origin;
    private String Destination;
    private int ClientId;
    private int DriverId;
    private String Description;
    private ZonedDateTime ServiceDatetime;
    private String CalendarEvent;
    private ZonedDateTime CalendarDatetime;
    private ZonedDateTime PayedDatetime;
    private Float BasePrice;
    private Float ExtraPrice;
    private ZonedDateTime ConfirmedDatetime;
    private int Passengers;
    private String SpecialNeeds;
}
