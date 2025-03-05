package academy.devdojo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AnimeGetResponse {
//    @JsonProperty("id")
    private Long id;
//    @JsonProperty("name")
    private String name;
}
