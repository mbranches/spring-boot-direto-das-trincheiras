package academy.devdojo.repository;

import academy.devdojo.model.Anime;
import academy.devdojo.model.Producer;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    public static final List<Anime> ANIMES = new ArrayList<>(List.of(
            new Anime(1L, "Naruto"),
            new Anime(2L, "Dbz"),
            new Anime(3L, "Boku no Hero"),
            new Anime(4L, "Boruto")
            ));

    public List<Anime> findAll() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream()
                .filter(a -> a.getName().equals(name))
                .toList();
    }

    public Anime save(Anime producer) {
        ANIMES.add(producer);
        return producer;
    }

    public void delete(Anime producer) {
        ANIMES.remove(producer);

    }

    public void update(Anime producer) {
        delete(producer);
        save(producer);
    }
}
