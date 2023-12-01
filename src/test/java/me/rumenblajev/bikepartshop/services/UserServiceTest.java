package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.enums.GenderEnum;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.dto.UserRegisterDTO;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.service.RegisterUserServiceModel;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Autowired
    private UserService subject;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;

    @Test
    void test_registerAdminUser() {
        var userRegisterDto = mockUserRegisterDto();
        var result = subject.registerAdminUser(userRegisterDto);
        assertEquals(userRegisterDto.getUsername(), result.getUsername());
        assertEquals(userRegisterDto.getFirstName(), result.getFirstName());
        assertEquals(userRegisterDto.getLastName(), result.getLastName());
        assertEquals(userRegisterDto.getAge(), result.getAge());
        assertEquals(userRegisterDto.getEmail(), result.getEmail());
        assertEquals(userRegisterDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(userRegisterDto.getGender(), result.getGender());
        assertEquals(RolesEnum.ADMIN, result.getRole().getName());
    }

    @Test
    void test_registerRegularUser() {
        var userRegisterDto = mockUserRegisterDto();
        var result = subject.registerRegularUser(userRegisterDto);
        assertEquals(userRegisterDto.getUsername(), result.getUsername());
        assertEquals(userRegisterDto.getFirstName(), result.getFirstName());
        assertEquals(userRegisterDto.getLastName(), result.getLastName());
        assertEquals(userRegisterDto.getAge(), result.getAge());
        assertEquals(userRegisterDto.getEmail(), result.getEmail());
        assertEquals(userRegisterDto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(userRegisterDto.getGender(), result.getGender());
        assertEquals(RolesEnum.USER, result.getRole().getName());
    }

    @Test
    void test_changeUserRole_swapsUserToAdminRole() {
        var user = mockUser();

        user.setId(2L);
        user.setRole(roleService.findRoleByName(RolesEnum.USER));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        subject.changeUserRole(user.getId());

        verify(userRepository,times(1)).save(any());
    }

    @Test
    void test_changeUserRole_swapsAdminToUserRole() {
        var user = mockUser();

        user.setId(2L);
        user.setRole(roleService.findRoleByName(RolesEnum.ADMIN));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        subject.changeUserRole(user.getId());

        verify(userRepository,times(1)).save(any());
    }

    @Test
    void test_register_savesUser() {
        var registerUserServiceModel = new RegisterUserServiceModel();
        var user = new User();

        registerUserServiceModel.setUsername("username");
        registerUserServiceModel.setFirstName("firstName");

        user.setUsername("username");
        user.setFirstName("firstName");

        subject.register(registerUserServiceModel);

        verify(userRepository, times(1)).saveAndFlush(any());
        when(userRepository.findOneByUsername("username")).thenReturn(Optional.of(user));

        var result = userRepository.findOneByUsername("username").get();

        assertEquals("username", result.getUsername());
        assertEquals("firstName", result.getFirstName());
    }

    @Test
    void test_findByUsername_returnsUser() {
        var user = mockUser();
        when(userRepository.findOneByUsername("username")).thenReturn(Optional.of(user));

        var result = subject.findByUsername("username");

        assertEquals(user, result.get());
    }

    @Test
    void test_userAlreadyExists_returnsTrueWhenUsernameExists() {
        var user = mockUser();
        when(userRepository.findOneByUsername("username")).thenReturn(Optional.of(user));

        var result = subject.userAlreadyExists(mockUserRegisterDto());

        assertEquals(true, result);
    }

    @Test
    void test_userAlreadyExists_returnsTrueWhenEmailExists() {
        var user = mockUser();
        when(userRepository.findOneByEmail("email@mail.bg")).thenReturn(Optional.of(user));

        var result = subject.userAlreadyExists(mockUserRegisterDto());

        assertEquals(true, result);
    }

    @Test
    void test_userAlreadyExists_returnsFalseWhenUserDoesNotExist() {
        when(userRepository.findOneByUsername("username")).thenReturn(Optional.empty());
        when(userRepository.findOneByEmail("email@mail.bg")).thenReturn(Optional.empty());

        var result = subject.userAlreadyExists(mockUserRegisterDto());

        assertEquals(false, result);
    }

    @Test
    void test_findAllUsers_returnsAllUsers() {
        var user = mockUser();
        when(userRepository.findAll()).thenReturn(java.util.List.of(user));

        var result = subject.findAllUsers();

        assertEquals(1, result.size());
        assertEquals(user.getUsername(), result.get(0).getUsername());
        assertEquals(user.getFirstName(), result.get(0).getFirstName());
        assertEquals(user.getLastName(), result.get(0).getLastName());
    }

    @Test
    void test_deleteUserById_deletesUser() {
        var user = mockUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        subject.deleteUserById(1L);

        verify(userRepository, times(1)).delete(any());
    }

    private User mockUser() {
        var user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setAge(42);
        user.setEmail("email@mail.bg");
        user.setPassword("password");
        user.setPhoneNumber("088111111");

        return user;
    }

    private UserRegisterDTO mockUserRegisterDto() {
        var userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("username");
        userRegisterDTO.setFirstName("firstName");
        userRegisterDTO.setLastName("lastName");
        userRegisterDTO.setAge(42);
        userRegisterDTO.setEmail("email@mail.bg");
        userRegisterDTO.setPassword("password");
        userRegisterDTO.setConfirmPassword("password");
        userRegisterDTO.setPhoneNumber("088111111");
        userRegisterDTO.setGender(GenderEnum.MALE);
        return userRegisterDTO;
    }

}