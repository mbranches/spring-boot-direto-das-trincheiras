package academy.devdojo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    public static List<Anime> animes = new ArrayList<>(List.of(new Anime(1L, "Naruto"), new Anime(2L, "Dbz")));

    public static List<Anime> listAllAnimes() {
        return animes;
    }
}
