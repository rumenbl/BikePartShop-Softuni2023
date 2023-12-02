package me.rumenblajev.bikepartshop.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String index() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(!context.getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            return "redirect:/home";
        }
        return "index";
    }

    @GetMapping("/home")
    public String home(final Principal principal, final Model model) {
        var username = principal.getName();

        model.addAttribute("username", username);

        return "home";
    }

    @ModelAttribute("username")
    public String username() {
        return "";
    }
}
