package academy.devdojo.utils;

import academy.devdojo.model.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {
    public List<Producer> newProducerList() {
        String dateTime = "2025-03-09T11:05:34.1311715";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

        Producer ufotable = Producer.builder().id(1L).name("Ufotable").createdAt(localDateTime).build();
        Producer witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(localDateTime).build();
        Producer studioGiblin = Producer.builder().id(3L).name("Studio Giblin").createdAt(localDateTime).build();

        return new ArrayList<>(List.of(ufotable, witStudio, studioGiblin));
    }

    public Producer newProducerToSave() {
        return Producer.builder().id(99L).name("MAPPA").createdAt(LocalDateTime.now()).build();
    }
}
