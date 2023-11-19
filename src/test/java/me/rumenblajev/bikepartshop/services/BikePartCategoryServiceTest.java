package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.repositories.BikePartCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BikePartCategoryServiceTest {
    @Autowired
    private BikePartCategoryService bikePartCategoryService;
    @Autowired
    private BikePartCategoryRepository bikePartCategoryRepository;

    @DisplayName("initCategories() should initialize the categories if there are less than 4 categories in the database")
    @Test
    void initCategoriesTest_whenUnder4_ShouldCreate() {
        bikePartCategoryService.initCategories();
        assertEquals(4, bikePartCategoryRepository.findAll().stream().count());
    }

    @DisplayName("initCategories() should not initialize the categories if there are 4 or more categories in the database")
    @Test
    void initCategoriesTest_when4OrMore_ShouldNotCreate() {
        bikePartCategoryService.initCategories();
        bikePartCategoryService.initCategories();
        assertEquals(4, bikePartCategoryRepository.findAll().stream().count());
    }
}