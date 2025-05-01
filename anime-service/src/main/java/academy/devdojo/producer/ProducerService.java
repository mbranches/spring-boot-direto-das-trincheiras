package academy.devdojo.producer;

import academy.devdojo.exception.NotFoundException;
import academy.devdojo.model.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {
    private final ProducerHardCodedRepository repository;

    @Autowired
    public ProducerService(ProducerHardCodedRepository repository) {
        this.repository = repository;
    }

    public List<Producer> findAll(String name) {
         return name == null ? repository.findAll() : repository.findByName(name);
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
        Producer originalProducer = findByIdOrThrowNotFound(producerToUpdated.getId());

        producerToUpdated.setCreatedAt(originalProducer.getCreatedAt());

        repository.update(producerToUpdated);
    }
}
