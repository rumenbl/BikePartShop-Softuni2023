package me.rumenblajev.bikepartshop.repositories;

import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;
import me.rumenblajev.bikepartshop.models.entity.BikePartCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BikePartCategoryRepository extends JpaRepository<BikePartCategory, Long> {
    Optional<BikePartCategory> findByName(BikePartCategoryEnum name);
}
