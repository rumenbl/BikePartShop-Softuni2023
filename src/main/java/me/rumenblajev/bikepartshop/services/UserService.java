package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.enums.GenderEnum;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.dto.UserRegisterDTO;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.service.RegisterUserServiceModel;
import me.rumenblajev.bikepartshop.models.view.UserViewModel;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ModelMapper modelMapper;

    public void initAdminUser() {
        if(this.userRepository.count() == 0){
            User admin = new User();
            admin.setUsername("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Adminov");
            admin.setAge(99);
            admin.setEmail("admin@admin.bg");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setGender(GenderEnum.MALE);
            admin.setPhoneNumber("+359881234567");
            admin.setRole(roleService.findRoleByName(RolesEnum.ADMIN));
            this.userRepository.save(admin);
        }
    }
    public RegisterUserServiceModel registerAdminUser(final UserRegisterDTO userRegisterDTO) {
        RegisterUserServiceModel registerUserServiceModel = this.modelMapper
                .map(userRegisterDTO, RegisterUserServiceModel.class);

        registerUserServiceModel.setRole(this.roleService.findRoleByName(RolesEnum.ADMIN));
        registerUserServiceModel.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));
        return registerUserServiceModel;
    }
    public RegisterUserServiceModel registerRegularUser(final UserRegisterDTO userRegisterDTO) {
        RegisterUserServiceModel registerUserServiceModel = this.modelMapper
                .map(userRegisterDTO, RegisterUserServiceModel.class);

        registerUserServiceModel.setRole(this.roleService.findRoleByName(RolesEnum.USER));
        registerUserServiceModel.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));
        return registerUserServiceModel;
    }
    public void changeUserRole(final Long id) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            switch (user.getRole().getName()) {
                case USER -> user.setRole(roleService.findRoleByName(RolesEnum.ADMIN));
                case ADMIN -> user.setRole(roleService.findRoleByName(RolesEnum.USER));
            }
            userRepository.save(user);
        }
    }
    public void register(final RegisterUserServiceModel registerUserServiceModel) {
        User user = this.modelMapper.map(registerUserServiceModel, User.class);
        userRepository.saveAndFlush(user);
    }

    public Optional<User> findByUsername(String username) {
        return this.userRepository.findOneByUsername(username);
    }

    public boolean userAlreadyExists(final UserRegisterDTO dto){
        return this.userRepository.findOneByUsername(dto.getUsername()).isPresent() ||
                this.userRepository.findOneByEmail(dto.getEmail()).isPresent();
    }

    public List<UserViewModel> findAllUsers() {
        return userRepository.findAll().stream().map(
                user -> modelMapper.map(user, UserViewModel.class)
        ).toList();
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
