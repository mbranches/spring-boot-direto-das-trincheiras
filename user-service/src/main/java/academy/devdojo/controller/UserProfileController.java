package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.model.UserProfile;
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
    private final UserMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfile>> findAll() {
        List<UserProfile> userProfiles = service.findAll();

        return ResponseEntity.ok(userProfiles);
    }
}
