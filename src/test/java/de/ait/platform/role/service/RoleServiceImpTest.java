package de.ait.platform.role.service;

import de.ait.platform.role.entity.Role;
import de.ait.platform.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
class RoleServiceImpTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImp roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
    }

    @Test
    void getRoleByTitle_whenRoleExists() {
        // Given: подготавливаем тестовые данные
        String roleTitle = "ADMIN";
        Role role = new Role();
        role.setTitle(roleTitle);

        // Мокируем метод репозитория
        when(roleRepository.findRoleByTitle(roleTitle)).thenReturn(role);

        // When: вызываем метод сервиса
        Role foundRole = roleService.getRoleByTitle(roleTitle);

        // Then: проверяем результат
        assertNotNull(foundRole);
        assertEquals(roleTitle, foundRole.getTitle());

        // Проверяем, что метод репозитория был вызван один раз
        verify(roleRepository, times(1)).findRoleByTitle(roleTitle);
    }

    @Test
    void getRoleByTitle_whenRoleDoesNotExist() {
        // Given: роль с таким заголовком отсутствует
        String roleTitle = "NON_EXISTENT_ROLE";

        // Мокируем метод репозитория, чтобы возвращал null
        when(roleRepository.findRoleByTitle(roleTitle)).thenReturn(null);

        // When: вызываем метод сервиса
        Role foundRole = roleService.getRoleByTitle(roleTitle);

        // Then: проверяем, что результат null, т.к. роль не найдена
        assertNull(foundRole);

        // Проверяем, что метод репозитория был вызван один раз
        verify(roleRepository, times(1)).findRoleByTitle(roleTitle);
    }
}
