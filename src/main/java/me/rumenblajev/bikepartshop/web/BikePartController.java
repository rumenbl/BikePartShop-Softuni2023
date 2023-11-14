package me.rumenblajev.bikepartshop.web;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.services.BikePartService;
import me.rumenblajev.bikepartshop.services.CartItemsService;
import me.rumenblajev.bikepartshop.services.CartService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/parts")
public class BikePartController {
    private final BikePartService bikePartService;
    private final CartService cartService;
    private final CartItemsService cartItemsService;
    private final UserService userService;
    @GetMapping("/all")
    public String viewAllParts(Model model) {
        List<PartViewModel> parts = bikePartService.findAllPartsViewModel();
        model.addAttribute("parts", parts);
        return "bike-parts";
    }

    @GetMapping("/category/{query}")
    public String searchPartsByCategory(Model model,@PathVariable String query) {
        List<PartViewModel> parts = bikePartService.findAllPartsByCategory(query);
        model.addAttribute("parts", parts);
        return "bike-parts";
    }

    @GetMapping("/cart/add/{partId}")
    public String addPartToCart(@PathVariable Integer partId) {
        return "cart";
    }

    @GetMapping("/cart/remove/{partId}")
    public String removePartFromCart(@PathVariable Integer partId) {
        return "cart";
    }

    @GetMapping("/cart")
    public String viewCart() {
        return "cart";
    }

    @ModelAttribute("parts")
    public PartViewModel getParts() {
        return new PartViewModel();
    }

}
