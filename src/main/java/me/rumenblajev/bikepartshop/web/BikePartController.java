package me.rumenblajev.bikepartshop.web;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.services.BikePartService;
import me.rumenblajev.bikepartshop.services.CartItemsService;
import me.rumenblajev.bikepartshop.services.CartService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
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
    public String viewAllParts(final Model model) {
        List<PartViewModel> parts = bikePartService.findAllPartsViewModel();
        model.addAttribute("parts", parts);
        return "bike-parts";
    }

    @GetMapping("/category/{query}")
    public String searchPartsByCategory(final Model model,
                                        final @PathVariable String query) {

        List<PartViewModel> parts = bikePartService.findAllPartsByCategory(query);
        model.addAttribute("parts", parts);
        return "bike-parts";
    }

    @GetMapping("/cart/add/{partId}")
    public String addPartToCart(final Principal principal,
                                final @PathVariable Long partId) {

        User user = userService.findByUsername(principal.getName()).get();
        cartService.addPartToCart(user, partId);

        return "redirect:/parts/cart";
    }

    @GetMapping("/cart/remove/{cartItemId}")
    public String removePartFromCart(final @PathVariable Long cartItemId) {
        final var cartItems = cartItemsService.getCartItems();
        if(cartItems != null) {
            cartItemsService.removeById(cartItemId);
        }
        return "redirect:/parts/cart";
    }

    @GetMapping("/cart")
    public String viewCart(final Model model,
                           final Principal principal) {

        final var user = userService.findByUsername(principal.getName()).get();
        final var cart = cartService.getOpenUserCart(user);

        if(cart.isEmpty()) {
            return "redirect:/parts/all";
        }

        final var cartItems = cartService.getCartContent(cart.get().getId());
        if(cartItems.isEmpty()) {
            return "redirect:/parts/all";
        }

        model.addAttribute("cartContent", cartItems);
        return "cart";
    }

    @ModelAttribute("parts")
    public PartViewModel getParts() {
        return new PartViewModel();
    }

    @ModelAttribute("cartContent")
    public CartItems getCartContent() {
        return new CartItems();
    }
}
