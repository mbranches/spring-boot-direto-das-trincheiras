package academy.devdojo.utils;

import academy.devdojo.model.Profile;
import academy.devdojo.model.User;
import academy.devdojo.model.UserProfile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class UserProfileUtils {
    private final UserUtils userUtils;
    private final ProfileUtils profileUtils;

    public UserProfileUtils(UserUtils userUtils, ProfileUtils profileUtils) {
        this.userUtils = userUtils;
        this.profileUtils = profileUtils;
    }

    public List<UserProfile> newUserProfileList() {
        User user = userUtils.newUserList().getFirst();
        Profile profile = profileUtils.newProfileList().getFirst();

        UserProfile userProfile = UserProfile.builder().id(1L).user(user).profile(profile).build();

        return new ArrayList<>(Collections.singletonList(userProfile));
    }

    public UserProfile newUserProfileToSave() {
        Profile profile = profileUtils.newProfileList().getLast();
        User user = userUtils.newUserList().getLast();

        return UserProfile.builder().profile(profile).user(user).build();
    }

    public UserProfile newUserProfileSave() {
        return newUserProfileToSave().withId(4L);
    }
}
