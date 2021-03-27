
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "type",
        "departure",
        "arrival",
        "summary",
        "transport"
})
public class Section {

    @JsonProperty("id")
    public String id;
    @JsonProperty("type")
    public String type;
    @JsonProperty("departure")
    public Departure departure;
    @JsonProperty("arrival")
    public Arrival arrival;
    @JsonProperty("summary")
    public Summary summary;
    @JsonProperty("transport")
    public Transport transport;

}
