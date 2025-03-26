package academy.devdojo.repository;

import academy.devdojo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {
    private final UserData userData;

    public List<User> findAll() {
        return userData.getUsers();
    }

    public List<User> findAllByName(String firstName) {
        List<User> users = userData.getUsers();
        return users.stream()
                .filter(user -> user.getFirstName().equalsIgnoreCase(firstName))
                .toList();
    }

    public Optional<User> findById(Long id) {
        List<User> users = userData.getUsers();
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
}
