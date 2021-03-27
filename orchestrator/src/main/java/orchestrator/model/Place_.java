
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "location",
        "originalLocation"
})
public class Place_ {

    @JsonProperty("type")
    public String type;
    @JsonProperty("location")
    public Location_ location;
    @JsonProperty("originalLocation")
    public OriginalLocation_ originalLocation;

}
