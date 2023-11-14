package me.rumenblajev.bikepartshop.init;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.services.RoleService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    @Override
    public void run(String... args) throws Exception {
        roleService.initRoles();
        userService.initAdminUser();
    }
}
