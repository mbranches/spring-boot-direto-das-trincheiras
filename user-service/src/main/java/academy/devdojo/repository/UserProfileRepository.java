package academy.devdojo.repository;

import academy.devdojo.model.Profile;
import academy.devdojo.model.User;
import academy.devdojo.model.UserProfile;
import academy.devdojo.response.UserProfileUserGetResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query(value = "SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p")
    List<UserProfile> retrieveAll();

//    @EntityGraph(attributePaths = {"user", "profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();

    @Query("SELECT up.user from UserProfile up where up.profile.id = ?1 ")
    List<User> findAllUsersByProfileId(Long profileId);
}
