package academy.devdojo.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ThreadLocalRandom;

@Setter
@Getter
public class ProducerPostRequest {
    private String name;
}
