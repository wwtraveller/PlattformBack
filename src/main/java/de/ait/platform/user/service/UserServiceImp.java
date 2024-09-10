package de.ait.platform.user.service;

import de.ait.platform.article.dto.ResponseArticle;
import de.ait.platform.article.entity.Article;
import de.ait.platform.article.exception.ArticleNotFound;
import de.ait.platform.user.dto.UserRequestDto;
import de.ait.platform.user.dto.UserResponseDto;
import de.ait.platform.user.entity.User;
import de.ait.platform.user.exceptions.UserNotFound;
import de.ait.platform.user.reposittory.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service

public class UserServiceImp implements UserService{
private final UserRepository repository;
private final ModelMapper mapper;

// todo remove constructors
    public UserServiceImp(UserRepository repository, ModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


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
        Article entity = repository.save(user);
        return null; //mapper.map(entity, UserResponseDto.class);
    }

    @Override
    public UserResponseDto deleteUser(Long id) {
        Optional<User> foundedUser = repository.findById(id);
        repository.deleteById(id);
        return mapper.map(foundedUser, UserResponseDto.class);
    }

    @Override
    public List<UserResponseDto> getUser() {
        List<User> users = repository.findAll();
        return users.stream().map(u->mapper.map(u,UserResponseDto.class)).toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return mapper.map(user.get(), UserResponseDto.class);
        }
        else {
            String message = "User with id: " + id + " not found";
            throw new UserNotFound(message);
        }
    }

    @Override
    public List<UserResponseDto> getUserByTitle(String title) {
        Predicate<User> predicateByTitle =
                (title.equals("")) ? u-> true:  user -> user.getTitle().equalsIgnoreCase(title);
        List<User> userList = repository.findAll().stream().filter(predicateByTitle).toList();
        return userList.stream().map(user -> mapper.map(user, UserResponseDto.class)).toList();
    }
}
