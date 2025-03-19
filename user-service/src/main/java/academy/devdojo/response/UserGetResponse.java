package academy.devdojo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
