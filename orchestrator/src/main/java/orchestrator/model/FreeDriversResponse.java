package orchestrator.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FreeDriversResponse {

    @JsonProperty("DriversIds")
    private List<Integer> driversIds;
    @JsonProperty("DriversNames")
    private List<String> driversNames;

}
