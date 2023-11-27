package me.rumenblajev.bikepartshop.web;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.enums.ShoppingCurrencyEnum;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.services.BikePartService;
import me.rumenblajev.bikepartshop.services.CartItemsService;
import me.rumenblajev.bikepartshop.services.CartService;
import me.rumenblajev.bikepartshop.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private ShoppingCurrencyEnum shoppingCurrency = ShoppingCurrencyEnum.BGN;

    @GetMapping("/all")
    public String viewAllParts(@RequestParam(value = "shoppingCurrency", required = false, defaultValue = "BGN")
            final String shoppingCurrencyStr,
                               final Model model) {
        List<PartViewModel> parts = bikePartService.findAllPartsViewModel();

        parts.forEach(
            part -> part.setPrice(part.getPrice() * ShoppingCurrencyEnum.valueOf(shoppingCurrencyStr).getValue())
        );

        model.addAttribute("parts", parts);
        return "bike-parts";
    }

    @GetMapping("/category/{query}")
    public String searchPartsByCategory(@RequestParam(value = "shoppingCurrency", required = false, defaultValue = "BGN")
                                            final String shoppingCurrencyStr,
                                        final Model model,
                                        final @PathVariable String query) {

        List<PartViewModel> parts = bikePartService.findAllPartsByCategory(query);
        parts.forEach(
                part -> part.setPrice(part.getPrice() * ShoppingCurrencyEnum.valueOf(shoppingCurrencyStr).getValue())
        );

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
    public String viewCart(@RequestParam(value = "shoppingCurrency", required = false, defaultValue = "BGN")
                               final String shoppingCurrencyStr,
                           final Model model,
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

        cartItems.forEach(
                cartItem -> cartItem.getPart().setPrice(cartItem.getPart().getPrice()* ShoppingCurrencyEnum.valueOf(shoppingCurrencyStr).getValue())
        );

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
