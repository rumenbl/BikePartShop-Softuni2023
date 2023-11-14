package me.rumenblajev.bikepartshop.web;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
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
    public String home() {
        return "home";
    }
}
