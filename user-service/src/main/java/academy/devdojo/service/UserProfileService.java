package academy.devdojo.service;

import academy.devdojo.model.User;
import academy.devdojo.model.UserProfile;
import academy.devdojo.repository.UserProfileRepository;
import academy.devdojo.response.UserProfileUserGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return repository.findAll();
    }

    public UserProfile save(UserProfile profile) {
        return repository.save(profile);
    }

    public List<User> findAllUsersByProfileId(Long profileId) {
        List<User> users = repository.findAllUsersByProfileId(profileId);
        return users;
    }
}
