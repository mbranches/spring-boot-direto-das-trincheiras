package academy.devdojo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Producer {
    private Long id;
    @JsonProperty("full_name")
    private String name;
    public static List<Producer> producers = new ArrayList<>(List.of(new Producer(1L, "Mappa"), new Producer(2L, "Kyoto Animation"), new Producer(3L, "MadHouse")));

    public static List<Producer> getProducers() {
        return producers;
    }
}
