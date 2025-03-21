package academy.devdojo.utils;

import academy.devdojo.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    public List<User> newUserList() {
        User marcelo = User.builder().id(1L).firstName("Marcelo").lastName("Viana").email("marcelo@gmai.com").build();
        User vinicius = User.builder().id(2L).firstName("Vinicius").lastName("Lima").email("vinicius@gmai.com").build();
        User mario = User.builder().id(3L).firstName("Mario").lastName("Lobado").email("mario@gmai.com").build();

        return new ArrayList<>(List.of(marcelo, vinicius, mario));
    }

    public User newUserToBeSaved() {
        return User.builder()
                .id(1L)
                .firstName("Marcelo")
                .lastName("Viana")
                .email("marcelo@gmai.com")
                .build();
    }
}
