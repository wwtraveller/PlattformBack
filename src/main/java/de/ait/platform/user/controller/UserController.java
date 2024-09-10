package de.ait.platform.user.controller;


import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/users")
public class UserController {
    private final UserService service;


    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto dto) {
        return service.createUser(dto);
    }

    @GetMapping
    public List<UserResponseDto> getUser() {
        return service.getUser();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable(name = "id") long id) {
        return service.getUserById(id);
    }


}
