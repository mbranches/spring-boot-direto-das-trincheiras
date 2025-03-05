package academy.devdojo.controller;

import academy.devdojo.model.Producer;
import academy.devdojo.requests.ProducerPostRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {

    @GetMapping
    public List<Producer> listAll(@RequestParam(required = false) String name) {
        List<Producer> producers = Producer.getProducers();

        if (name == null) return producers;

        return producers.stream().filter(a -> a.getName().equals(name)).toList();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
            headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);
        var producer = Producer.builder()
                .id(ThreadLocalRandom.current().nextLong(100_000))
                .name(producerPostRequest.getName())
                .createdAt(LocalDateTime.now())
                .build();

        Producer.getProducers().add(producer);

        var response = ProducerGetResponse.builder()
                .id(producer.getId())
                .name(producer.getName())
                .createdAt(producer.getCreatedAt())
                .build();


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.getProducers().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }
}
