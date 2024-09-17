package de.ait.platform.user.service;


import de.ait.platform.role.entity.Role;
import de.ait.platform.role.service.RoleService;
import de.ait.platform.user.dto.UserLoginDto;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.exceptions.UserNotFound;
import de.ait.platform.user.reposittory.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository repository;
    private final ModelMapper mapper;
    private final RoleService roleService;
    private final BCryptPasswordEncoder encoder;



    @Override
    public UserResponseDto createUser(UserRequestDto dto) {
        repository.findUserByUsername(dto.getUsername()).ifPresent(u -> {
            throw new RuntimeException("User " + u.getUsername() + " already exists");
        });

        Role role = roleService.getRoleByTitle("ROLE_USER");
        HashSet<Role> setRole = new HashSet<>();
        setRole.add(role);
        String encodedPassword = encoder.encode(dto.getPassword());
        User newUser = repository.save(new User(
                null,dto.getUsername(),  dto.getFirstName(), dto.getLastName(), dto.getEmail(),encodedPassword,dto.getPhoto(), setRole ));
        return mapper.map(newUser, UserResponseDto.class);

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
        } else {
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

    @Override
    public UserResponseDto setAdminRole(String email, boolean admin) {
        return null;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + username + " not found"));
    }
}
