package academy.devdojo.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required.")
    private String name;
}
