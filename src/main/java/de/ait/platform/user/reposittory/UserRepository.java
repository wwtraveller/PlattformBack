package de.ait.platform.user.reposittory;

import de.ait.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   public User findByEmail(String email);
   public Optional<User> findUserByUsername(String username);

}
