package academy.devdojo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
public class Producer {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private static List<Producer> producers = new ArrayList<>();
    static {
        Producer mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        Producer kyoto = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        Producer madHouse = Producer.builder().id(3L).name("MadHouse").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(mappa, kyoto, madHouse));
    }

    public static List<Producer> getProducers() {
        return producers;
    }

    public static void addProducer(Producer producer) {
        Producer.producers.add(producer);
    }
}
