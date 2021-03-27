
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "label",
        "countryCode",
        "countryName",
        "stateCode",
        "state",
        "county",
        "city",
        "postalCode"
})
public class Address {

    @JsonProperty("label")
    public String label;
    @JsonProperty("countryCode")
    public String countryCode;
    @JsonProperty("countryName")
    public String countryName;
    @JsonProperty("stateCode")
    public String stateCode;
    @JsonProperty("state")
    public String state;
    @JsonProperty("county")
    public String county;
    @JsonProperty("city")
    public String city;
    @JsonProperty("postalCode")
    public String postalCode;

}
