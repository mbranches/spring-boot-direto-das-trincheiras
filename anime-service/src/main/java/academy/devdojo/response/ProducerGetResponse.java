package academy.devdojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ProducerGetResponse {
    private Long id;
    @JsonProperty("name")
    private String name;
    private LocalDateTime createdAt;
}
