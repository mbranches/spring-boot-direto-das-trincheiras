package academy.devdojo.anime;

import academy.devdojo.model.Anime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("v1/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper MAPPER;
    private final AnimeService SERVICE;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        List<Anime> animes = SERVICE.findAll(name);

        List<AnimeGetResponse> animeGetResponse = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponse);
    }

    //v1/animes/paginated?size=3&page=3&sort=id,desc -> exemplo de parametros disponiveis
    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> findAllPaginated(Pageable pageable) {
        Page<AnimeGetResponse> animes = SERVICE.findAllPageable(pageable).map(MAPPER::toAnimeGetResponse);

        return ResponseEntity.ok(animes);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest animePostRequest, @RequestHeader HttpHeaders headers) {
        Anime anime = MAPPER.toAnime(animePostRequest);

        Anime animeSaved = SERVICE.save(anime);

        AnimePostResponse response = MAPPER.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        Anime anime = SERVICE.findByIdOrThrowNotFound(id);

        AnimeGetResponse animeGetResponse = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        SERVICE.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        Anime animeUpdated = MAPPER.toAnime(request);

        SERVICE.update(animeUpdated);

        return ResponseEntity.noContent().build();
    }
}
