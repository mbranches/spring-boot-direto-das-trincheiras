package academy.devdojo.response;

import lombok.Data;

@Data
public class ProfileGetResponse {
    private Long id;
    private String name;
    private String description;
}
