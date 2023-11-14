package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;
import me.rumenblajev.bikepartshop.models.entity.BikePartCategory;
import me.rumenblajev.bikepartshop.repositories.BikePartCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class BikePartCategoryService {
    private final BikePartCategoryRepository bikePartCategoryRepository;
    public void initCategories() {
        if(bikePartCategoryRepository.count()<4) {
            Arrays.stream(BikePartCategoryEnum.values()).forEach(category -> {
                BikePartCategory cat = new BikePartCategory();
                cat.setName(category);
                cat.setDescription(category.name());
                bikePartCategoryRepository.save(cat);
            });
        }
    }
}
