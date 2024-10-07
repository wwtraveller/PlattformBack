package de.ait.platform.role.repository;
import de.ait.platform.role.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Test saving and retrieving a Role by title")
    void testFindRoleByTitle() {
        // Given: создаем и сохраняем новую роль
        Role adminRole = new Role();
        adminRole.setTitle("ADMIN");
        roleRepository.save(adminRole);

        // When: находим роль по заголовку
        Role foundRole = roleRepository.findRoleByTitle("ADMIN");

        // Then: проверяем, что найденная роль не null и имеет ожидаемый заголовок
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getTitle()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Test retrieving Role that doesn't exist")
    void testFindRoleByTitleNotFound() {
        // When: пытаемся найти роль, которой нет
        Role foundRole = roleRepository.findRoleByTitle("USER");

        // Then: убеждаемся, что результат null, так как роли нет в базе
        assertThat(foundRole).isNull();
    }

    @Test
    @DisplayName("Test saving and retrieving multiple roles")
    void testSaveAndRetrieveMultipleRoles() {
        // Given: создаем и сохраняем несколько ролей
        Role adminRole = new Role();
        adminRole.setTitle("ADMIN");

        Role userRole = new Role();
        userRole.setTitle("USER");

        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        // When: находим роли по заголовкам
        Role foundAdminRole = roleRepository.findRoleByTitle("ADMIN");
        Role foundUserRole = roleRepository.findRoleByTitle("USER");

        // Then: проверяем, что обе роли найдены и правильные
        assertThat(foundAdminRole).isNotNull();
        assertThat(foundAdminRole.getTitle()).isEqualTo("ADMIN");

        assertThat(foundUserRole).isNotNull();
        assertThat(foundUserRole.getTitle()).isEqualTo("USER");
    }

}