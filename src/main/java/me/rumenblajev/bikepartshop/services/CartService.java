package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.CartItemsRepository;
import me.rumenblajev.bikepartshop.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;

    public Cart validate(final User user) {
        return cartRepository.findByUserAndStatus(user,"open").orElse(null);
    }

    public List<CartItems> getCartContent(final Long cartId) {
        return cartItemsRepository.findCartItemsByCartId(cartId);
    }
    public void save(Cart cart, CartItems items) {
        cartRepository.save(cart);
        cartItemsRepository.save(items);
    }
    public void saveCartItems(CartItems items) {
        cartItemsRepository.save(items);
    }
}
