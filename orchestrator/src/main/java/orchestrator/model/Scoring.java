
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "queryScore",
        "fieldScore"
})
public class Scoring {

    @JsonProperty("queryScore")
    public Double queryScore;
    @JsonProperty("fieldScore")
    public FieldScore fieldScore;

}
