package de.ait.platform.user.service;


import de.ait.platform.category.dto.CategoryResponse;
import de.ait.platform.category.entity.Category;
import de.ait.platform.category.exceptions.CategoryNotFound;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.exceptions.UserNotFound;
import de.ait.platform.user.reposittory.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository repository;
    private final ModelMapper mapper;


    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        User entity = mapper.map(dto, User.class); // dto превращаем в entity


        entity = repository.save(entity);
        UserResponseDto userResponseDto = mapper.map(entity, UserResponseDto.class);
        return userResponseDto;
    }


    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto dto) {
        User user = mapper.map(dto, User.class);
        user.setId(id);
        User entity = repository.save(user);
        return mapper.map(entity, UserResponseDto.class);
    }

    @Override
    public UserResponseDto deleteUser(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            repository.deleteById(id);
        }
        else {
            throw new UserNotFound("Error deleting user. Couldn't find user with id:" + id);
        }
        return mapper.map(user, UserResponseDto.class);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        List<User> users = repository.findAll();
        return users.stream().map(u -> mapper.map(u, UserResponseDto.class)).toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return mapper.map(user.get(), UserResponseDto.class);
        } else {
            String message = "User with id: " + id + " not found";
            throw new UserNotFound(message);
        }
    }

    @Override
    public List<UserResponseDto> getUserByEmail(String email) {
        Predicate<User> predicateByEmail =
                (email.equals("")) ? u -> true : user -> user.getEmail().equalsIgnoreCase(email);
        List<User> userList = repository.findAll().stream().filter(predicateByEmail).toList();
        return userList.stream().map(user -> mapper.map(user, UserResponseDto.class)).toList();
    }
}
