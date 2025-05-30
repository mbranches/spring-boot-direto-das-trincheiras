package academy.devdojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetResponse {
    @Schema(description = "User id", example = "1")
    private Long id;
    @Schema(description = "User first name", example = "Luan")
    private String firstName;
    @Schema(description = "User last name", example = "Silva")
    private String lastName;
    @Schema(description = "User email. Must be unique in the system.", example = "luan.silva@gmail.com")
    private String email;
}
