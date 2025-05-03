package academy.devdojo.utils;

import academy.devdojo.model.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileUtils {
    public List<Profile> newProfileList() {
        Profile admin = Profile.builder().id(1L).name("Administrator").description("Admins everything").build();
        Profile manager = Profile.builder().id(2L).name("Manager").description("Managers users").build();

        return new ArrayList<>(List.of(admin, manager));
    }

    public Profile newProfileToBeSaved() {
        return Profile.builder()
                .name("Regular User")
                .description("Regular User with regular permissions")
                .build();
    }

    public Profile newProfileSaved() {
        return Profile.builder()
                .id(5L)
                .name("Regular User")
                .description("Regular User with regular permissions")
                .build();
    }
}
