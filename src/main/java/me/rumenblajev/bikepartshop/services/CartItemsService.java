package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import me.rumenblajev.bikepartshop.repositories.CartItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CartItemsService {
    private final CartItemsRepository cartItemsRepository;

    public Optional<CartItems> findPartInCart(final Cart cart, final BikePart part){
        return cartItemsRepository.findByPartIdAndCartId(part.getId(), cart.getId());
    }

    public List<CartItems> getCartItems() {
        return cartItemsRepository.findAll();
    }

    public void removeById(Long cartItemId) {
        cartItemsRepository.deleteById(cartItemId);
    }

    public List<CartItems> findItemsByUser(final User user) {
        return cartItemsRepository.findAllByCart_UserAndCartStatus(user, "open");
    }

    public void saveCartItems(final CartItems cartItems) {
        cartItemsRepository.save(cartItems);
    }

    public void closeUserCartItems(final User user) {
        cartItemsRepository.findAllByCart_UserAndCartStatus(user, "pending")
                .forEach(cartItems -> cartItems.getCart().setStatus("closed"));
    }
}
