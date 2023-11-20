package me.rumenblajev.bikepartshop.web;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.enums.RolesEnum;
import me.rumenblajev.bikepartshop.models.dto.RoleChangeDTO;
import me.rumenblajev.bikepartshop.models.view.UserViewModel;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/users")
public class UserAdministrationController {

    private final UserService userService;

    @GetMapping("/all")
    public String allUsers(final Model model) {
        model.addAttribute("allUsers", userService.findAllUsers());

        return "users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(final @PathVariable Long id) {
        userService.deleteUserById(id);

        return "redirect:/administration/users/all";
    }

    @GetMapping("/change-role/{id}")
    public String changeUserRole(final @PathVariable Long id) {
        userService.changeUserRole(id);

        return "redirect:/administration/users/all";
    }

    @ModelAttribute("allUsers")
    public UserViewModel allUsers() {
        return new UserViewModel();
    }

    @ModelAttribute("roleChangeDTO")
    public RoleChangeDTO roleChangeDTO() {
        return new RoleChangeDTO();
    }

    @ModelAttribute("allRoles")
    public RolesEnum[] allRoles() {
        return RolesEnum.values();
    }
}
