package academy.devdojo.anime;

import academy.devdojo.model.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findAllByNameContaining(String name);
}
