package academy.devdojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class Anime {
    private Long id;
    private String name;

    public static List<Anime> listAllAnimes() {
        return List.of(new Anime(1L, "Naruto"), new Anime(2L, "Dbz"));
    }
}
