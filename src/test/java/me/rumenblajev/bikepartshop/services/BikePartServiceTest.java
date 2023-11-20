package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;
import me.rumenblajev.bikepartshop.models.dto.PartCreateDTO;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.repositories.BikePartCategoryRepository;
import me.rumenblajev.bikepartshop.repositories.BikePartRepository;
import me.rumenblajev.bikepartshop.repositories.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BikePartServiceTest {
    @Autowired
    private BikePartService bikePartService;
    @Autowired
    private BikePartCategoryRepository bikePartCategoryRepository;
    @Autowired
    private BikePartRepository bikePartRepository;
    @Autowired
    private BrandRepository brandRepository;
    @BeforeEach
    void setUp() {
        PartCreateDTO partCreateDTO = new PartCreateDTO();
        partCreateDTO.setTitle("title1");
        partCreateDTO.setPrice(1.0);
        partCreateDTO.setStock(1);
        partCreateDTO.setBrand("brand1");
        partCreateDTO.setCategory(BikePartCategoryEnum.BRAKES);
        partCreateDTO.setPictureUrl("pictureUrl1");
        bikePartService.savePart(partCreateDTO);
    }
    @Test
    void test_findAllPartsViewModel_returnsCorrect() {
        PartViewModel partViewModel = new PartViewModel();
        partViewModel.setId(1L);
        partViewModel.setTitle("title1");
        partViewModel.setPrice(1.0);
        partViewModel.setStock(1);
        partViewModel.setBrand("brand1");
        partViewModel.setCategory(bikePartCategoryRepository.findByName(BikePartCategoryEnum.BRAKES).get());
        partViewModel.setPictureUrl("pictureUrl1");

        PartViewModel partViewModel1 = bikePartService.findAllPartsViewModel().get(0); // theres only one from the setup.
        assertEquals(partViewModel.getId(), partViewModel1.getId());
        assertEquals(partViewModel.getTitle(), partViewModel1.getTitle());
        assertEquals(partViewModel.getDescription(), partViewModel1.getDescription());
        assertEquals(partViewModel.getPrice(), partViewModel1.getPrice());
        assertEquals(partViewModel.getStock(), partViewModel1.getStock());
        assertEquals(partViewModel.getBrand(), partViewModel1.getBrand());
        assertEquals(partViewModel.getCategory().getName(), partViewModel1.getCategory().getName());
        assertEquals(partViewModel.getPictureUrl(), partViewModel1.getPictureUrl());
    }

    @Test
    void test_findAllPartsByCategory_returnAllWithEmptyQuery() {
        assertEquals(1, bikePartService.findAllPartsByCategory("").size());
    }
    @Test
    void test_findAllPartsByCategory_returnNoneWithWrongQuery() {
        assertEquals(0, bikePartService.findAllPartsByCategory("veryTestingOfMe").size());
    }
    @Test
    void test_findAllPartsByCategory_returnOneWithGoodQuery() {
        assertEquals(1, bikePartService.findAllPartsByCategory("BRAKES").size());
    }
    @Test
    void test_findPartViewModelById_throwsExceptionWhenBadId() {
        assertThrows(IllegalArgumentException.class, () ->bikePartService.findPartViewModelById(2L));
    }

    @Test
    void test_findPartViewModelById_returnsModelWhenGoodId() {
        PartViewModel partViewModel = new PartViewModel();
        partViewModel.setId(1L);
        partViewModel.setTitle("title1");
        partViewModel.setPrice(1.0);
        partViewModel.setStock(1);
        partViewModel.setBrand("brand1");
        partViewModel.setCategory(bikePartCategoryRepository.findByName(BikePartCategoryEnum.BRAKES).get());
        partViewModel.setPictureUrl("pictureUrl1");

        PartViewModel actual = bikePartService.findPartViewModelById(1L);
        assertEquals(partViewModel.getId(), actual.getId());
        assertEquals(partViewModel.getTitle(), actual.getTitle());
        assertEquals(partViewModel.getDescription(), actual.getDescription());
        assertEquals(partViewModel.getPrice(), actual.getPrice());
        assertEquals(partViewModel.getStock(), actual.getStock());
        assertEquals(partViewModel.getBrand(), actual.getBrand());
        assertEquals(partViewModel.getCategory().getName(), actual.getCategory().getName());
        assertEquals(partViewModel.getPictureUrl(), actual.getPictureUrl());
    }

    @Test
    void test_findById_returnsNullWithBadId() {
        assertNull(bikePartService.findById(2L));
    }

    @Test
    void test_findById_returnsPartWithGoodId() {
        assertNotNull(bikePartService.findById(1L));
    }

    @Test
    void test_deletePart_doesntDeleteWithBadId() {
        bikePartService.deletePart(2L);
        assertEquals(1, bikePartService.findAllPartsViewModel().size());
    }

    @Test
    void test_deletePart_deletesPartWithValidId() {
        bikePartService.deletePart(1L);
        assertEquals(0, bikePartService.findAllPartsViewModel().size());
    }

    @Test
    void test_saveEditedPart_throwsExceptionWithBadId() {
        PartViewModel partViewModel = new PartViewModel();
        partViewModel.setId(1L);
        partViewModel.setTitle("title1");
        partViewModel.setPrice(1.0);
        partViewModel.setStock(1);
        partViewModel.setBrand("brand1");
        partViewModel.setCategory(bikePartCategoryRepository.findByName(BikePartCategoryEnum.BRAKES).get());
        partViewModel.setPictureUrl("pictureUrl1");

        assertThrows(NullPointerException.class,
                () -> bikePartService.saveEditedPart(partViewModel, 2L));
    }

    @Test
    void test_saveEditedPart_savesPartWhenGivenValidId() {
        PartViewModel partViewModel = new PartViewModel();
        partViewModel.setId(1L);
        partViewModel.setTitle("title1");
        partViewModel.setPrice(1.0);
        partViewModel.setStock(10);
        partViewModel.setBrand("brand1");
        partViewModel.setCategory(bikePartCategoryRepository.findByName(BikePartCategoryEnum.BRAKES).get());
        partViewModel.setPictureUrl("pictureUrl1");

        bikePartService.saveEditedPart(partViewModel,1L);
        assertEquals(10, bikePartService.findById(1L).getStock());
    }

    @Test
    void test_checkIfPartTitleAlreadyExists_returnsTrueWhenGivenExistingTitle() {
        assertTrue(bikePartService.checkIfPartTitleAlreadyExists("title1"));
    }

    @Test
    void test_checkIfPartTitleAlreadyExists_returnsFalseWhenGivenNewTitle() {
        assertFalse(bikePartService.checkIfPartTitleAlreadyExists("title2"));
    }

}