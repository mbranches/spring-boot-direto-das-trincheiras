package academy.devdojo.service;

import academy.devdojo.model.UserProfile;
import academy.devdojo.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.retrieveAll();
    }

    public UserProfile save(UserProfile profile) {
        return repository.save(profile);
    }
}
