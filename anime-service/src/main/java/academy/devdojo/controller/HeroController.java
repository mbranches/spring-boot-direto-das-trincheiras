package academy.devdojo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {
    private static final List<String> HEROES = List.of("Spider man", "Kakashi", "Iron man", "Zoro", "Naruto");

    @GetMapping
    public List<String> listAllHeroes() {
        return HEROES;
    }

    @GetMapping("filter")               //http://localhost:8080/v1/heroes/filter?name=
    public List<String> listAllHeroesParam(@RequestParam(defaultValue = "") String name) {
        return HEROES.stream()
                .filter(h -> h.equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping("filterList")               //http://localhost:8080/v1/heroes/filter?name=
    public List<String>listAllHeroesParamList(@RequestParam(defaultValue = "") List<String> names) {
        return HEROES.stream()
                .filter(names::contains)
                .toList();
    }

    @GetMapping("{name}")
    public String findByName(@PathVariable() String name) {
        return HEROES.stream()
                .filter(name::equalsIgnoreCase)
                .findFirst()
                .orElse("Anime not found");
    }
}
