package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.entity.Role;
import me.rumenblajev.bikepartshop.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public void initRoles(){
        if(this.roleRepository.count() == 0) {
            Role admin = new Role();
            admin.setName(RolesEnum.ADMIN);
            Role user = new Role();
            user.setName(RolesEnum.USER);
            this.roleRepository.saveAll(
                    List.of(user, admin)
            );
        }
    }

    public Role findRoleByName(RolesEnum name){
        return this.roleRepository.findByName(name).orElse(null);
    }
}
