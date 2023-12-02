package me.rumenblajev.bikepartshop.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.dto.UserRegisterDTO;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.view.UserViewModel;
import me.rumenblajev.bikepartshop.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private static final String USER_REGISTER_REDIRECT_PATH = "redirect:/users/register";
    private static final String USER_LOGIN_REDIRECT_PATH = "redirect:/users/login";

    @GetMapping("/register")
    public String registerGet() {
        return "register";
    }

    @GetMapping("/home")
    public String home(){
        return "redirect:/home";
    }

    @GetMapping("")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/profile")
    public String userProfile(final Principal principal,
                              final Model model) {

        String username = principal.getName();
        User user = this.userService.findByUsername(username).get();

        UserViewModel userViewModel = this.modelMapper.map(user, UserViewModel.class);
        model.addAttribute("userProfile", userViewModel);

        return "user-profile";
    }

    @ModelAttribute("passwordMustMatch")
    public boolean passwordMatch() {
        return true;
    }

    @ModelAttribute("userAlreadyExists")
    public boolean userExists() {
        return false;
    }

    @PostMapping("/register")
    public String registerPost(final @Valid UserRegisterDTO userRegisterDTO,
                               final BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDTO", bindingResult);
            return USER_REGISTER_REDIRECT_PATH;
        }

        if (userService.userAlreadyExists(userRegisterDTO)) {
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO);
            redirectAttributes.addFlashAttribute("userAlreadyExists", true);
            return USER_REGISTER_REDIRECT_PATH;
        }

        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("userRegisterDTO", userRegisterDTO);
            redirectAttributes.addFlashAttribute("passwordMustMatch", false);
            return USER_REGISTER_REDIRECT_PATH;
        }

        final var userRegisterModel = this.userService.registerRegularUser(userRegisterDTO);
        this.userService.register(userRegisterModel);
        return USER_LOGIN_REDIRECT_PATH;
    }

    @PostMapping("/login-failed")
    public String wrongCredentials(
            final @ModelAttribute(
                    UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            final RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(
                UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);

        redirectAttributes.addFlashAttribute("checkCredentials", true);

        return USER_LOGIN_REDIRECT_PATH;
    }

    @ModelAttribute("userRegisterDTO")
    public UserRegisterDTO userRegisterDTO() {
        return new UserRegisterDTO();
    }
}
