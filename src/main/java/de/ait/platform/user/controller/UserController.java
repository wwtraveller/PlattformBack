package de.ait.platform.user.controller;

import de.ait.platform.user.dto.UserPhotoUrlDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.service.SpaceService;
import de.ait.platform.user.service.UserService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
//@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService service;

    private final SpaceService spaceService;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }


    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))})
    })
    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return service.getUsers();

    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("users/{id}")
    public UserResponseDto getUserById(@PathVariable(name = "id") Long id) {
        return service.getUserById(id);
    }

    @Operation(summary = "Update user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/users/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        return service.updateUser(id, dto);
    }

    @Operation(summary = "Delete user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/users/{id}")
    public UserResponseDto deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }

    @GetMapping("/check-username")
    public ResponseEntity<String> checkUsername(@RequestParam String username) {
        boolean exists = service.isUsernameTaken(username);

        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username is already taken");
        }

        return ResponseEntity.ok("Username is available");
    }


    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmail(@RequestParam String email) {
        boolean exists = service.isEmailTaken(email);
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already taken");
        }

        return ResponseEntity.ok("Email is available");
    }

    @Operation(summary = "Add a photo by URL")
    @PostMapping("/users/photo/url")
    public ResponseEntity<String> addPhotoByUrl(@RequestBody UserPhotoUrlDto photoUrlDto) {
        String response = service.addPhotoByUrl(photoUrlDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add a photo by file")
    @PostMapping("/users/photo/file")
    public ResponseEntity<String> addPhotoByFile(@RequestParam("photoFile") MultipartFile photoFile) {
        String response = service.addPhotoByFile(photoFile);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("avatar") MultipartFile file) throws IOException, java.io.IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        JpaSort.Path tempFile = (JpaSort.Path) Files.createTempFile("temp", fileName);
        file.transferTo(((java.nio.file.Path) tempFile).toFile());
        String fileUrl = spaceService.uploadFile(fileName, (Path) tempFile);
        return ResponseEntity.ok(fileUrl);
    }

}














