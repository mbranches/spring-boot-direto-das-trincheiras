package academy.devdojo.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProducerPutRequest {
    @NotNull(message = "The field 'id' cannot be null.")
    private Long id;
    @NotBlank(message = "The field 'name' is required.")
    private String name;
}
