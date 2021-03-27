
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "west",
        "south",
        "east",
        "north"
})
public class MapView {

    @JsonProperty("west")
    public Double west;
    @JsonProperty("south")
    public Double south;
    @JsonProperty("east")
    public Double east;
    @JsonProperty("north")
    public Double north;

}
