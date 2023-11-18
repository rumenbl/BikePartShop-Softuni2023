package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.CartItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartItemsService {
    private final CartItemsRepository cartItemsRepository;

    public CartItems findPartInCart(Cart cart, BikePart part){
        return cartItemsRepository.findByPartIdAndCartId(part.getId(), cart.getId());
    }

    public List<CartItems> getCartItems() {
        return cartItemsRepository.findAll();
    }

    public void removeById(Long cartItemId) {
        cartItemsRepository.deleteById(cartItemId);
    }

    public List<CartItems> findItemsByUser(User user) {
        return cartItemsRepository.findAllByCart_UserAndCartStatus(user, "open");
    }
}
