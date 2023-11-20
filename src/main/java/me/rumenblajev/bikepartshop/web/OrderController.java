package me.rumenblajev.bikepartshop.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.dto.OrderCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.services.OrderService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/create")
    public String createOrder() {
        return "order-info";
    }

    @PostMapping("/create")
    public String createOrderPost(final @Valid OrderCreateDTO orderCreateDTO,
                                  final BindingResult bindingResult,
                                  final RedirectAttributes redirectAttributes,
                                  final Principal principal) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderCreateDTO", orderCreateDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.orderCreateDTO", bindingResult
            );

            return "redirect:/orders/create";
        }

        final String username = principal.getName();
        final User user = userService.findByUsername(username).get();

        orderService.createUserOrder(orderCreateDTO, user);

        return "thank-you";
    }

    @ModelAttribute("orderCreateDTO")
    public OrderCreateDTO orderCreateDTO() {
        return new OrderCreateDTO();
    }
}
