package academy.devdojo.producer;

import academy.devdojo.exception.NotFoundException;
import academy.devdojo.model.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository repository;

    public List<Producer> findAll(String name) {
         return name == null ? repository.findAll() : repository.findAllByNameContaining((name));
    }

    public Producer findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Producer not found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(Producer producerToUpdated) {
        assertProducerExists(producerToUpdated.getId());
        repository.save(producerToUpdated);
    }

    public void assertProducerExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
}
