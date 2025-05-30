package academy.devdojo.controller;

import academy.devdojo.exception.DefaultErrorMessage;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.model.User;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @Operation(
            summary = "Get all users",
            description = "Get all users available in the system",
            responses = {
                    @ApiResponse(
                            description = "List all users",
                            responseCode = "200",
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class)
                            )
                    ))
            }
    )
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        List<User> users = service.findAll(firstName);

        List<UserGetResponse> userGetResponse = mapper.toUserGetResponseList(users);

        return ResponseEntity.ok(userGetResponse);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(
                            description = "Get user by its id",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGetResponse.class)
                            )
                    ),
                    @ApiResponse(
                            description = "User Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        User returnedUser = service.findByIdOrThrowsNotFoundException(id);
        UserGetResponse response = mapper.toUserGetResponse(returnedUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "Create user",
            responses = {
                    @ApiResponse(
                            description = "Save user in the database",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserPostRequest.class)
                            )
                    ),
                    @ApiResponse(
                            description = "User Not Found",
                            responseCode = "404",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)
                            )
                    )
            }
    )
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
