package me.rumenblajev.bikepartshop.web;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.Cart;
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
    public String addPartToCart(Principal principal, @PathVariable Long partId) {
        User user = userService.findByUsername(principal.getName());
        Cart cart = cartService.validate(user);
        BikePart part = bikePartService.findById(partId);
        if(cart == null || cart.getStatus().equals("closed")) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            CartItems cartItems = new CartItems();
            cartItems.setPart(part);
            cartItems.setAmount(1);
            cartItems.setCart(newCart);
            cartService.save(newCart, cartItems);
        } else {
            CartItems cartItems = cartItemsService.findPartInCart(cart, part);
            if(cartItems == null) {
                CartItems cartItem = new CartItems();
                cartItem.setCart(cart);
                cartItem.setPart(part);
                cartItem.setAmount(1);
                cartService.save(cart,cartItem);
            } else {
                cartItems.setAmount(cartItems.getAmount()+1);
                cartService.saveCartItems(cartItems);
            }
        }
        return "redirect:/parts/cart";
    }

    @GetMapping("/cart/remove/{cartItemId}")
    public String removePartFromCart(@PathVariable Long cartItemId) {
        List<CartItems> cartItems = cartItemsService.getCartItems();
        if(cartItems != null) {
            cartItemsService.removeById(cartItemId);
        }
        return "redirect:/parts/cart";
    }

    @GetMapping("/cart")
    public String viewCart(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Cart cart = cartService.validate(user);
        if(cart == null) {
            return "redirect:/parts/all";
        }
        List<CartItems> cartItems = cartService.getCartContent(cart.getId());
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
