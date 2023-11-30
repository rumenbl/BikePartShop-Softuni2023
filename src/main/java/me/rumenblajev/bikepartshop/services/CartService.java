package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.CartItemsRepository;
import me.rumenblajev.bikepartshop.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemsService cartItemsService;
    private final CartItemsRepository cartItemsRepository;
    private final BikePartService bikePartService;

    public Optional<Cart> getOpenUserCart(final User user) {
        return cartRepository.findByUserAndStatus(user, "open");
    }

    public List<CartItems> getCartContent(final Long cartId) {
        return cartItemsRepository.findCartItemsByCartId(cartId);
    }

    public void save(final Cart cart, final CartItems items) {
        cartRepository.save(cart);
        cartItemsRepository.save(items);
    }

    public void closeUserCart(final User user) {
        var cart = cartRepository.findByUserAndStatus(user, "open");
        if (cart.isPresent()) {
            cart.get().setStatus("closed");
            cartRepository.save(cart.get());
        }
    }
    private void createAndSaveCartItems(final Cart cart, final BikePart part) {
        CartItems cartItems = new CartItems();
        cartItems.setCart(cart);
        cartItems.setPart(part);
        cartItems.setAmount(1);
        save(cart, cartItems);
    }

    private void incrementCartItems(final CartItems cartItems) {
        cartItems.setAmount(cartItems.getAmount()+1);
        cartItemsService.saveCartItems(cartItems);
    }

    public void addPartToCart(final User user, final Long partId) {
        final var cart = getOpenUserCart(user);
        final var part = bikePartService.findById(partId).get();

        if(cart.isEmpty()) {
            Cart newCart = new Cart();
            newCart.setUser(user);
            createAndSaveCartItems(newCart, part);
        } else {
            var cartItems = cartItemsService.findPartInCart(cart.get(), part);

            if(cartItems.isEmpty()) {
                createAndSaveCartItems(cart.get(), part);
            } else {
                incrementCartItems(cartItems.get());
            }

        }
    }

    public void deleteAllCartsForUser(final User user) {
        cartRepository.findAll().stream().filter(cart ->
                cart.getUser().getId().equals(user.getId())
        ).forEach(cartRepository::delete);
    }
}
