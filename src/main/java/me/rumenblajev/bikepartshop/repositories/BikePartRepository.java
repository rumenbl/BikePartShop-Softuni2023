package me.rumenblajev.bikepartshop.repositories;

import me.rumenblajev.bikepartshop.models.entity.BikePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BikePartRepository extends JpaRepository<BikePart,Long> {
    Optional<BikePart> findByTitleIgnoreCase(String title);
}
