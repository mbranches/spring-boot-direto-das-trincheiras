package academy.devdojo.controller;

import academy.devdojo.mapper.UserProfileMapper;
import academy.devdojo.model.User;
import academy.devdojo.model.UserProfile;
import academy.devdojo.response.UserProfileGetResponse;
import academy.devdojo.response.UserProfileUserGetResponse;
import academy.devdojo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        List<UserProfile> userProfiles = service.findAll();

        List<UserProfileGetResponse> response = mapper.toUserProfileGetResponseList(userProfiles);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/profiles/{id}/users")
    public ResponseEntity<List<UserProfileUserGetResponse>> findAllUsersByProfileId(@PathVariable Long id) {
        List<User> userList = service.findAllUsersByProfileId(id);

        List<UserProfileUserGetResponse> response = mapper.toUserProfileUserGetResponseList(userList);

        return ResponseEntity.ok(response);
    }
}
