package academy.devdojo.controller;

import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.model.Producer;
import academy.devdojo.repository.ProducerHardCodedRepository;
import academy.devdojo.requests.ProducerPostRequest;
import academy.devdojo.requests.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping("v1/producers")
public class ProducerController {
    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping
    public List<ProducerGetResponse> listAll(@RequestParam(required = false) String name) {
        List<ProducerGetResponse> producerGetResponseList = MAPPER.toProducerGetResponseList(Producer.getProducers());

        if (name == null) return producerGetResponseList;

        return producerGetResponseList.stream().filter(a -> a.getName().equals(name)).toList();
    }

    @PostMapping(headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);
        Producer producer = MAPPER.toProducer(producerPostRequest);

        Producer.addProducer(producer);


        ProducerGetResponse response = MAPPER.toProducerGetResponse(producer);


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("{id}")
    public ProducerGetResponse findById(@PathVariable Long id) {
        return
                .map(MAPPER::toProducerGetResponse).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not Found"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {

        Producer producerToBeDeleted = producers.stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producer not Found"));

        .delete(producerToBeDeleted);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        Producer produceToBeDeleted = producers.stream()
                .filter(p -> p.getId().equals(request.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produce not Found"));

        Producer producerUpdated = MAPPER.toProducer(request, produceToBeDeleted.getCreatedAt());

        producers.remove(produceToBeDeleted);
        producers.add(producerUpdated);

        return ResponseEntity.noContent().build();
    }
}
