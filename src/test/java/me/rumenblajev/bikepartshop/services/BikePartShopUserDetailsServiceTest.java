package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.entity.Role;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BikePartShopUserDetailsServiceTest {
    @Autowired
    private BikePartShopUserDetailsService subject;
    @MockBean
    private UserRepository userRepository;

    @Test
    void test_loadUserByUsername_loadsRegularUser() {
        var user = mockUser();
        var role = new Role();

        role.setName(RolesEnum.USER);
        user.setRole(role);

        when(userRepository.findOneByUsername("admin")).thenReturn(Optional.of(user));

        var userDetails = subject.loadUserByUsername("admin");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void test_loadUserByUsername_loadsAdminUser() {
        var user = mockUser();
        var role = new Role();

        role.setName(RolesEnum.ADMIN);
        user.setRole(role);

        when(userRepository.findOneByUsername("admin")).thenReturn(Optional.of(user));

        var userDetails = subject.loadUserByUsername("admin");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void test_loadUserByUsername_throwsExceptionWhenUserNotFound() {
        when(userRepository.findOneByUsername("admin")).thenReturn(Optional.empty());

        try {
            subject.loadUserByUsername("admin");
        } catch (Exception e) {
            assertEquals("User not found", e.getMessage());
        }
    }

    private User mockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setFirstName("Admin");
        user.setLastName("Adminov");
        user.setAge(99);
        user.setEmail("email@mail.bg");
        user.setPassword("admin");
        user.setPhoneNumber("+359881234567");
        return user;
    }
}