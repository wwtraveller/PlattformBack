package de.ait.platform.user.reposittory;

import de.ait.platform.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    public void setUp() {
        testUser1 = User.builder().email("test1@example.com").username("testuser1").password("password1").build();
        testUser2 = User.builder().email("test2@example.com").username("testuser2").password("password2").build();

        userRepository.save(testUser1);
        userRepository.save(testUser2);
    }

    @Test
    public void UserRepository_ExistsByEmail_ReturnsTrue() {


        boolean exists = userRepository.existsByEmail("test1@example.com");
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void UserRepository_ExistsByUsername_ReturnsTrue() {

        boolean exists = userRepository.existsByUsername("testuser1");
        Assertions.assertThat(exists).isTrue();
    }


    @Test
    public void UserRepository_FindUserByUsername_ReturnsUser() {

        Optional<User> foundUser = userRepository.findUserByUsername("testuser1");
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getUsername()).isEqualTo("testuser1");
    }

    @Test
    public void UserRepository_FindUserById_ReturnsUser() {


        Optional<User> foundUser = userRepository.findUserById(testUser1.getId());
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getId()).isEqualTo(testUser1.getId());
    }

    @Test
    public void UserRepository_FindByEmail_ReturnsUser() {

        User foundUser = userRepository.findByEmail("test1@example.com");
        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    public void UserRepository_FindAll_ReturnListUsers() {

        List<User> users = userRepository.findAll();
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_UpdateUser_ReturnUpdatedUser() {

        User foundUser = userRepository.findById(testUser1.getId()).get();
        foundUser.setEmail("updated@example.com");
        User updatedUser = userRepository.save(foundUser);
        Assertions.assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    public void UserRepository_DeleteUser_ReturnUserIsEmpty() {

        userRepository.deleteById(testUser1.getId());
        Optional<User> deletedUser = userRepository.findById(testUser1.getId());
        Assertions.assertThat(deletedUser).isEmpty();
    }
}


