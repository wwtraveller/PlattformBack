package de.ait.platform.user.controller;



import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService service;


    @PostMapping("/users")
    public UserResponseDto createUser(@RequestBody UserLoginDto dto) {
        return service.createUser(dto);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return service.getUsers();
    }

    @GetMapping("users/{id}")
    public UserResponseDto getUserById(@PathVariable(name = "id") long id) {
        return service.getUserById(id);
    }


    @PutMapping("/users/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        return service.updateUser(id, dto);
    }

    @DeleteMapping("/users/{id}")
    public UserResponseDto deleteUser(@PathVariable Long id) {
        return service.deleteUser(id);
    }
}








