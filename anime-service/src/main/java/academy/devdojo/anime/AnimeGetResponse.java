package academy.devdojo.anime;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnimeGetResponse {
//    @JsonProperty("id")
    private Long id;
//    @JsonProperty("name")
    private String name;
}
