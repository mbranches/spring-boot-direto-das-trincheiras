package academy.devdojo.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
