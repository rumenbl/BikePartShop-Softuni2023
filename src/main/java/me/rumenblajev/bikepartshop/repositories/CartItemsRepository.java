package me.rumenblajev.bikepartshop.repositories;

import me.rumenblajev.bikepartshop.models.entity.CartItems;
import me.rumenblajev.bikepartshop.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    List<CartItems> findCartItemsByCartId(Long cartId);
    Optional<CartItems> findByPartIdAndCartId(Long partId, Long cartId);
    List<CartItems> findAllByCart_UserAndCartStatus(User user, String status);
}
