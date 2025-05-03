package academy.devdojo.controller;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.model.Profile;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("v1/profiles")
@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;
    
    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        List<Profile> profileList = service.findAll();

        List<ProfileGetResponse> response = mapper.toProfileGetResponseList(profileList);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest profilePostRequest) {
        Profile profileToBeSaved = mapper.toProfile(profilePostRequest);

        Profile profileSaved = service.save(profileToBeSaved);

        ProfilePostResponse response = mapper.toProfilePostResponse(profileSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
