package academy.devdojo.producer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required.")
    private String name;
}
