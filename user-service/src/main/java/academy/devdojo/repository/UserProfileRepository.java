package academy.devdojo.repository;

import academy.devdojo.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query(value = "SELECT up FROM UserProfile up JOIN FETCH up.user u JOIN FETCH up.profile p")
    List<UserProfile> retrieveAll();
}
