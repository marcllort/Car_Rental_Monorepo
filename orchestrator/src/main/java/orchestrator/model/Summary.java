
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "duration",
        "length",
        "baseDuration"
})
public class Summary {

    @JsonProperty("duration")
    public Integer duration;
    @JsonProperty("length")
    public Integer length;
    @JsonProperty("baseDuration")
    public Integer baseDuration;

}
