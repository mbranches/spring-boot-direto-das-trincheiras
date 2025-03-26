package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.model.User;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest userPostRequest) {
        User userToBeSaved = mapper.toUser(userPostRequest);

        User userSaved = service.save(userToBeSaved);

        UserPostResponse response = mapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest userPutRequest) {
        User userToBeUpdated = mapper.toUser(userPutRequest);

        service.update(userToBeUpdated);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
