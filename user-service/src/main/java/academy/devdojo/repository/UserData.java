package academy.devdojo.repository;

import academy.devdojo.model.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class UserData {
    private final List<User> users = new ArrayList<>(3);

    {
        User goku = User.builder().id(1L).firstName("Goku").lastName("Son").email("goku@gmail.com").build();
        User luan = User.builder().id(1L).firstName("Luan").lastName("Silva").email("luan@gmail.com").build();
        User marcus = User.builder().id(1L).firstName("Marcus").lastName("Branches").email("marcus@gmail.com").build();

        users.addAll(List.of(goku, luan, marcus));
    }
}
