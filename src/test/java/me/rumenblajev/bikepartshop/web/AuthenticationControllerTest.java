package me.rumenblajev.bikepartshop.web;

import me.rumenblajev.bikepartshop.enums.GenderEnum;
import me.rumenblajev.bikepartshop.models.dto.UserRegisterDTO;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void test_registerGet_redirectsToHomePageWithAuthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/users/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void test_registerGet_returnsRegisterPageWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void test_home_returnsHomePageWhenAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void test_home_returnsLoginRedirectWhenNotAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @Test
    void test_index_redirectsToLoginWhenNotAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void test_index_returnsIndexPageWhenAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void test_login_redirectsToHomeWhenAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void test_login_returnsLoginPageWhenNotAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users/login"))
                .andExpect(status().isOk());
    }

    @Test
    void test_userProfile_returnsLoginPageWhenNotAuthenticated() throws Exception {
        this.mockMvc.perform(get("/users/profile"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/users/login"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void test_userProfile_returnsUserProfilePageWhenAuthenticated() throws Exception {
        var user = new User();
        user.setUsername("admin");

        when(userRepository.findOneByUsername("admin")).thenReturn(java.util.Optional.of(user));

        this.mockMvc.perform(get("/users/profile"))
                .andExpect(status().isOk());
    }

    @Test
    void test_registerPost_redirectsToLoginPageWithErrorWhenUserAlreadyExists() throws Exception {
        var userRegisterDTO = mockUserRegisterDTO();
        userRegisterDTO.setUsername("admin");
        this.mockMvc.perform(post("/users/register")
                        .with(csrf())
                .flashAttr("userRegisterDTO", userRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void test_registerPost_redirectsToRegisterPageWithErrorWhenUserRegisterDtoHasErrors() throws Exception {
        var userRegisterDTO = mockUserRegisterDTO();

        userRegisterDTO.setUsername("admin");
        userRegisterDTO.setPassword("11");
        userRegisterDTO.setConfirmPassword("11");

        this.mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeCount(2));
    }

    @Test
    void test_registerPost_redirectsToRegisterPageWithErrorWhenPasswordsDoesntMatch() throws Exception {
        var userRegisterDTO = mockUserRegisterDTO();

        userRegisterDTO.setUsername("admin");
        userRegisterDTO.setPassword("1123");
        userRegisterDTO.setConfirmPassword("1122");

        this.mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/register"))
                .andExpect(flash().attributeCount(2))
                .andExpect(flash().attribute("passwordMustMatch", false));
    }

    @Test
    void test_registerPost_registersUserWhenGivenValidUserDto() throws Exception {
        var userRegisterDTO = mockUserRegisterDTO();
        this.mockMvc.perform(post("/users/register")
                        .with(csrf())
                        .flashAttr("userRegisterDTO", userRegisterDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));

        verify(userRepository,times(1)).saveAndFlush(any());
    }

    private UserRegisterDTO mockUserRegisterDTO() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setUsername("user");
        userRegisterDTO.setFirstName("admin");
        userRegisterDTO.setLastName("admin");
        userRegisterDTO.setAge(20);
        userRegisterDTO.setEmail("email@mail.bg");
        userRegisterDTO.setPassword("1234");
        userRegisterDTO.setConfirmPassword("1234");
        userRegisterDTO.setPhoneNumber("123456789");
        userRegisterDTO.setGender(GenderEnum.MALE);

        return userRegisterDTO;
    }
}