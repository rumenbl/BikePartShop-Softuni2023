package me.rumenblajev.bikepartshop.repositories;

import me.rumenblajev.bikepartshop.models.entity.Cart;
import me.rumenblajev.bikepartshop.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Optional<Cart> findByUserAndStatus(User user, String status);
}
