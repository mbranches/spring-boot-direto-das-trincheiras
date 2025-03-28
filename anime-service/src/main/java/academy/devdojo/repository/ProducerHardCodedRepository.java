package academy.devdojo.repository;

import academy.devdojo.config.Connection;
import academy.devdojo.model.Producer;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProducerHardCodedRepository {
    private static final Logger log = LogManager.getLogger(ProducerHardCodedRepository.class);
    private final ProducerData producerData;
    private final Connection connection;

    public List<Producer> findAll() {
        log.info(connection);
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
