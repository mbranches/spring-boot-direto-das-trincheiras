package academy.devdojo.anime;

import academy.devdojo.exception.NotFoundException;
import academy.devdojo.model.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public List<Anime> findAll(String name) {
         return name == null ? repository.findAll() : repository.findAllByNameContaining((name));
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(Anime animeToUpdated) {
        findByIdOrThrowNotFound(animeToUpdated.getId());

        repository.save(animeToUpdated);
    }
}
