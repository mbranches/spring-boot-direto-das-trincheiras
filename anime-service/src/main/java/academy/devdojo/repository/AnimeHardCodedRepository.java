package academy.devdojo.repository;

import academy.devdojo.model.Anime;
import academy.devdojo.model.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AnimeHardCodedRepository {
    public final AnimeData animeData;

    public List<Anime> findAll() {
        return animeData.getANIMES();
    }

    public Optional<Anime> findById(Long id) {
        return animeData.getANIMES().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Anime> findByName(String name) {
        return animeData.getANIMES().stream()
                .filter(a -> a.getName().equals(name))
                .toList();
    }

    public Anime save(Anime producer) {
        animeData.getANIMES().add(producer);
        return producer;
    }

    public void delete(Anime producer) {
        animeData.getANIMES().remove(producer);

    }

    public void update(Anime producer) {
        delete(producer);
        save(producer);
    }
}
