
package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "sections"
})
public class Route_ {

    @JsonProperty("id")
    public String id;
    @JsonProperty("sections")
    public List<Section> sections = null;

}
