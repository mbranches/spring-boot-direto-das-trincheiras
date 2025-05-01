package academy.devdojo.anime;

import academy.devdojo.model.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {
    public List<Anime> newAnimeList() {
        Anime anime1 = new Anime(1L, "Attack on Titan");
        Anime anime2 = new Anime(2L, "Naruto");
        Anime anime3 = new Anime(3L, "Demon Slayer");

        return new ArrayList<>(List.of(anime1, anime2, anime3));
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(100L).name("novo anime").build();
    }
}
