package academy.devdojo.controller;

import academy.devdojo.mapper.AnimeMapper;
import academy.devdojo.model.Anime;
import academy.devdojo.requests.AnimePostRequest;
import academy.devdojo.response.AnimeGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/animes")
public class AnimeController {
    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    @GetMapping
    public List<AnimeGetResponse> listAll(@RequestParam(required = false) String name) {
        List<AnimeGetResponse> animeGetResponseList = MAPPER.toAnimeGetResponseList(Anime.animes);

        if (name == null) return animeGetResponseList;

        return animeGetResponseList.stream().filter(a -> a.getName().equals(name)).toList();
    }

    @PostMapping
    public ResponseEntity<AnimeGetResponse> addAnime(@RequestBody AnimePostRequest postRequest) {
        log.debug("{}", postRequest);
        Anime anime = MAPPER.toAnime(postRequest);
        Anime.animes.add(anime);
        AnimeGetResponse response = MAPPER.toAnimeGetResponse(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        return Anime.listAllAnimes().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not Found"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        List<Anime> animeList = Anime.listAllAnimes();

        Anime animeToBeDeleted = animeList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not Found"));

        animeList.remove(animeToBeDeleted);
        log.info(animeList.toString());
        log.info("{}", animeToBeDeleted);

        return ResponseEntity.noContent().build();
    }
}
