package academy.devdojo.service;

import academy.devdojo.exceptions.NotFoundException;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodedRepository repository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findAllByName(firstName);
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
