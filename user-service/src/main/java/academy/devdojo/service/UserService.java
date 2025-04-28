package academy.devdojo.service;

import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.model.User;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
    }

    public User findByIdOrThrowsNotFoundException(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User save(User user) {
        assertEmailDoesNotExists(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowsNotFoundException(id));
    }

    public void update(User user) {
        assertUserExists(user);
        assertEmailDoesNotExists(user.getEmail(), user.getId());
        repository.save(user);
    }

    public void assertUserExists(User user) {
        findByIdOrThrowsNotFoundException(user.getId());
    }

    public void assertEmailDoesNotExists(String email) {
        repository.findByEmail(email)
                .ifPresent(this::throwsEmailAlreadyExists);
    }

    public void assertEmailDoesNotExists(String email, Long id) {
        repository.findByEmailAndIdNot(email, id)
                .ifPresent(this::throwsEmailAlreadyExists);
    }

    private void throwsEmailAlreadyExists(User user) {
        throw new EmailAlreadyExistsException("E-mail '%s' already exists".formatted(user.getEmail()));
    }
}
