package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.model.User;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        List<User> users = service.findAll(firstName);

        List<UserGetResponse> userGetResponse = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        User returnedUser = service.findByIdOrThrowsNotFoundException(id);
        UserGetResponse response = mapper.toUserGetResponse(returnedUser);

        return ResponseEntity.ok(response);
    }
}
