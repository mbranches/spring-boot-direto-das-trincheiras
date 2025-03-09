package academy.devdojo.controller;

import academy.devdojo.mapper.ProducerMapper;
import academy.devdojo.model.Producer;
import academy.devdojo.requests.ProducerPostRequest;
import academy.devdojo.requests.ProducerPutRequest;
import academy.devdojo.response.ProducerGetResponse;
import academy.devdojo.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/producers")
@RequiredArgsConstructor  //cria um constutor para todos os atributos finais
public class ProducerController {
    private final ProducerMapper MAPPER;
    private final ProducerService service;

    @GetMapping
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        List<Producer> producers = service.findAll(name);

        List<ProducerGetResponse> producerGetResponse = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        Producer producer = MAPPER.toProducer(producerPostRequest);

        Producer producerSaved = service.save(producer);

        ProducerGetResponse response = MAPPER.toProducerGetResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        Producer producer = service.findByIdOrThrowNotFound(id);

        ProducerGetResponse producerGetResponse = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        Producer producerUpdated = MAPPER.toProducer(request);

        service.update(producerUpdated);

        return ResponseEntity.noContent().build();
    }
}
