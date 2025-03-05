package academy.devdojo.controller;

import academy.devdojo.model.Producer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    @GetMapping
    public List<Producer> listAll(@RequestParam(required = false) String name) {
        List<Producer> producers = Producer.producers;

        if (name == null) return producers;

        return producers.stream().filter(a -> a.getName().equals(name)).toList();
    }
                            //key    value
    @PostMapping(headers = "x-api-key=1234") //obriga a passar essa key
    public Producer addProducer(@RequestBody Producer producer) {
        producer.setId(ThreadLocalRandom.current().nextLong(1, 10));
        Producer.producers.add(producer);
        return producer;
    }

    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.getProducers().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }
}
