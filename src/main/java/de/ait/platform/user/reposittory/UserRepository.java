package de.ait.platform.user.reposittory;

import de.ait.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserById(Long id);

    User findByEmail(String email);

}
