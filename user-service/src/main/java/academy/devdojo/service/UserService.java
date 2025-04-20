package academy.devdojo.service;

import academy.devdojo.exception.NotFoundException;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserHardCodedRepository;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodedRepository repository;
    private final UserRepository userRepository;

    public List<User> findAll(String firstName) {
        return firstName == null ? userRepository.findAll() : repository.findAllByName(firstName);
    }

    public User findByIdOrThrowsNotFoundException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowsNotFoundException(id));
    }

    public void update(User user) {
        findByIdOrThrowsNotFoundException(user.getId());
        repository.update(user);
    }
}
