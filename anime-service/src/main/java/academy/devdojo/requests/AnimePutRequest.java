package academy.devdojo.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnimePutRequest {
    private Long id;
    private String name;
}
