package me.rumenblajev.bikepartshop.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.dto.OrderCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.Order;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.services.CartItemsService;
import me.rumenblajev.bikepartshop.services.CartService;
import me.rumenblajev.bikepartshop.services.OrderService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
@RequestMapping("/orders")
public class OrderController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CartItemsService cartItemsService;
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/create")
    public String createOrder() {
        return "order-info";
    }

    @PostMapping("/create")
    public String createOrderPost(@Valid OrderCreateDTO orderCreateDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes,
                                     Model model,
                                     Principal principal) {

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("orderCreateDTO", orderCreateDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.orderCreateDTO", bindingResult);
            return "redirect:/orders/create";
        }

        final String username = principal.getName();
        final User user = userService.findByUsername(username);

        Order order = modelMapper.map(orderCreateDTO, Order.class);
        order.setClient(user);
        order.setItems(cartItemsService.findItemsByUser(user));

        orderService.save(order);
        cartService.closeUserCart(user);
        //cartItemsService.closeItems(user);
        return "thank-you";
    }

    @ModelAttribute("orderCreateDTO")
    public OrderCreateDTO orderCreateDTO() {
        return new OrderCreateDTO();
    }
}
