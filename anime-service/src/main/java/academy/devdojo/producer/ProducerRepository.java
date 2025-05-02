package academy.devdojo.producer;

import academy.devdojo.model.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    List<Producer> findAllByNameContaining(String name);
}
