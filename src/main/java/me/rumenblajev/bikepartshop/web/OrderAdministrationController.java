package me.rumenblajev.bikepartshop.web;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.view.OrderViewModel;
import me.rumenblajev.bikepartshop.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/administration/orders")
public class OrderAdministrationController {
    private final OrderService orderService;
    @GetMapping("/all")
    public String allOrders(final Model model) {
        model.addAttribute("allOrders", orderService.findAllOrders());
        return "orders";
    }

    @ModelAttribute("allOrders")
    public OrderViewModel allOrders() {
        return new OrderViewModel();
    }
}
