package de.ait.platform.role.service;


import de.ait.platform.role.entity.Role;
import de.ait.platform.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleRepository repository;

    @Override
    public Role getRoleByTitle(String title) {
        return repository.findRoleByTitle(title);
    }
}
