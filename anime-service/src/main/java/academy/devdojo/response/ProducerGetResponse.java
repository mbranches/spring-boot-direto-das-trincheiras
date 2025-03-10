package academy.devdojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProducerGetResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
}
