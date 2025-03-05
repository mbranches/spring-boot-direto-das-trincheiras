package academy.devdojo.controller;

import academy.devdojo.model.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
public class AnimeController {

    @GetMapping
    public List<Anime> listAll(@RequestParam(required = false) String name) {
        List<Anime> animeList = Anime.listAllAnimes();

        if (name == null) return animeList;

        return animeList.stream().filter(a -> a.getName().equals(name)).toList();
    }

    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        return Anime.listAllAnimes().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }
}
