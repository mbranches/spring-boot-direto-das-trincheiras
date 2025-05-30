package academy.devdojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserPutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    @Schema(description = "User id", example = "1")
    private Long id;
    @NotBlank(message = "The field 'firstName' is required")
    @Schema(description = "User first name", example = "Luan")
    private String firstName;
    @Schema(description = "User last name", example = "Silva")
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;
    @Schema(description = "User email. Must be unique in the system.", example = "luan.silva@gmail.com")
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^(?!.*\\.\\.)([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})$", message = "email is not valid")
    private String email;
}
