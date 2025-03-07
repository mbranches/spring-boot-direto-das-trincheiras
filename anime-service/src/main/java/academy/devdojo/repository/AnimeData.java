package academy.devdojo.repository;

import academy.devdojo.model.Anime;
import academy.devdojo.model.Producer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {
    private final List<Anime> ANIMES = new ArrayList<>();
    {
        Anime naruto = Anime.builder().id(1L).name("Naruto").build();
        Anime dbz = Anime.builder().id(2L).name("Dbz").build();
        Anime bokuNoHero = Anime.builder().id(3L).name("Boku no Hero").build();
        Anime boruto = Anime.builder().id(4L).name("Boruto").build();
        ANIMES.addAll(List.of(naruto, dbz, bokuNoHero, boruto));
    }

    public List<Anime> getANIMES() {
        return ANIMES;
    }
}
