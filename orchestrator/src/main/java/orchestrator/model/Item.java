
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "title",
        "id",
        "resultType",
        "localityType",
        "address",
        "position",
        "mapView",
        "scoring"
})
public class Item {

    @JsonProperty("title")
    public String title;
    @JsonProperty("id")
    public String id;
    @JsonProperty("resultType")
    public String resultType;
    @JsonProperty("localityType")
    public String localityType;
    @JsonProperty("address")
    public Address address;
    @JsonProperty("position")
    public Position position;
    @JsonProperty("mapView")
    public MapView mapView;
    @JsonProperty("scoring")
    public Scoring scoring;

}
