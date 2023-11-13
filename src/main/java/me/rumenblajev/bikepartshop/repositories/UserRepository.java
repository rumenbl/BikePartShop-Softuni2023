package me.rumenblajev.bikepartshop.repositories;

import me.rumenblajev.bikepartshop.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findOneByUsername(String username);
}
