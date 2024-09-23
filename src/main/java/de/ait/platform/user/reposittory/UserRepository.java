package de.ait.platform.user.reposittory;

import de.ait.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//public interface UserRepository extends JpaRepository<User, Long> {
//
//   @Query("SELECT u FROM User u WHERE u.userName = :userName")
//   @Cacheable("users")
//   User findByUserName(@Param("userName") String userName);
//}
public interface UserRepository extends JpaRepository<User, Long> {
   public User findByEmail(String email);
   public Optional<User> findUserByUsername(String username);
   public Optional<User> findUserById(Long id);

}
