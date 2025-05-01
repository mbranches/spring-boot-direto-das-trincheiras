package academy.devdojo.producer;

import academy.devdojo.model.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {
    private final List<Producer> PRODUCERS = new ArrayList<>();
    {
        Producer mappa = Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        Producer kyoto = Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        Producer madHouse = Producer.builder().id(3L).name("MadHouse").createdAt(LocalDateTime.now()).build();
        PRODUCERS.addAll(List.of(mappa, kyoto, madHouse));
    }

    public List<Producer> getPRODUCERS() {
        return PRODUCERS;
    }
}
