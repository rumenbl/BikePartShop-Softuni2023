package me.rumenblajev.bikepartshop.repositories;

import me.rumenblajev.bikepartshop.models.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findByBrandNameIgnoreCase(String brand);
}
