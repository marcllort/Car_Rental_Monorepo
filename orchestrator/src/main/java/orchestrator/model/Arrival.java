
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "place"
})
public class Arrival {

    @JsonProperty("time")
    public String time;
    @JsonProperty("place")
    public Place_ place;

}
