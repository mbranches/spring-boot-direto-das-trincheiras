package academy.devdojo.repository;

import academy.devdojo.model.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProducerHardCodedRepository {
    private final ProducerData producerData;

    public List<Producer> findAll() {
        return producerData.getPRODUCERS();
    }

    public Optional<Producer> findById(Long id) {
        return producerData.getPRODUCERS().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }

    public List<Producer> findByName(String name) {
        return producerData.getPRODUCERS().stream()
                .filter(a -> a.getName().equals(name))
                .toList();
    }

    public Producer save(Producer producer) {
        producerData.getPRODUCERS().add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        producerData.getPRODUCERS().remove(producer);

    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }
}
