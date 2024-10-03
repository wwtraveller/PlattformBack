package de.ait.platform.role.repository;

import de.ait.platform.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findRoleByTitle(String title);
}
