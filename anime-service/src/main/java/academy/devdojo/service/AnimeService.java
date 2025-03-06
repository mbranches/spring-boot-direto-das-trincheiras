package academy.devdojo.service;

import academy.devdojo.model.Anime;
import academy.devdojo.repository.AnimeHardCodedRepository;
import academy.devdojo.repository.AnimeHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class AnimeService {
    AnimeHardCodedRepository repository;

    public AnimeService() {
        this.repository = new AnimeHardCodedRepository();
    }

    public List<Anime> findAll(String name) {
         return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(Anime animeToUpdated) {
        Anime originalAnime = findByIdOrThrowNotFound(animeToUpdated.getId());

        repository.update(animeToUpdated);
    }
}
