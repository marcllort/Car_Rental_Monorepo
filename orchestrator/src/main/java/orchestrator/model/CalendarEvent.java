package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "created",
        "creator",
        "end",
        "etag",
        "eventType",
        "htmlLink",
        "iCalUID",
        "id",
        "kind",
        "organizer",
        "originalStartTime",
        "recurringEventId",
        "reminders",
        "sequence",
        "start",
        "status",
        "summary",
        "transparency",
        "updated"
})

@Data
public class CalendarEvent {

    @JsonProperty("created")
    public String created;
    @JsonProperty("creator")
    public Creator creator;
    @JsonProperty("end")
    public End end;
    @JsonProperty("etag")
    public String etag;
    @JsonProperty("eventType")
    public String eventType;
    @JsonProperty("htmlLink")
    public String htmlLink;
    @JsonProperty("iCalUID")
    public String iCalUID;
    @JsonProperty("id")
    public String id;
    @JsonProperty("kind")
    public String kind;
    @JsonProperty("organizer")
    public Organizer organizer;
    @JsonProperty("originalStartTime")
    public OriginalStartTime originalStartTime;
    @JsonProperty("recurringEventId")
    public String recurringEventId;
    @JsonProperty("reminders")
    public Reminders reminders;
    @JsonProperty("sequence")
    public Integer sequence;
    @JsonProperty("start")
    public Start start;
    @JsonProperty("status")
    public String status;
    @JsonProperty("summary")
    public String summary;
    @JsonProperty("transparency")
    public String transparency;
    @JsonProperty("updated")
    public String updated;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "email",
            "self"
    })
    @Data
    public static class Creator {

        @JsonProperty("email")
        public String email;
        @JsonProperty("self")
        public Boolean self;

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "date"
    })
    @Data
    public static class End {

        @JsonProperty("date")
        public String date;

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "email",
            "self"
    })
    @Data
    public static class Organizer {

        @JsonProperty("email")
        public String email;
        @JsonProperty("self")
        public Boolean self;

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "date"
    })
    @Data
    public static class OriginalStartTime {

        @JsonProperty("date")
        public String date;

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "method",
            "minutes"
    })
    public static class Override {

        @JsonProperty("method")
        public String method;
        @JsonProperty("minutes")
        public Integer minutes;

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "overrides"
    })
    @Data
    public static class Reminders {

        @JsonProperty("overrides")
        public List<Override> overrides = null;

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "date"
    })
    @Data
    public static class Start {

        @JsonProperty("date")
        public String date;

    }
}


