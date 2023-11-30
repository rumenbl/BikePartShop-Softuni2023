package me.rumenblajev.bikepartshop.services;

import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;
import me.rumenblajev.bikepartshop.models.dto.PartCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.BikePartCategory;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.repositories.BikePartCategoryRepository;
import me.rumenblajev.bikepartshop.repositories.BikePartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BikePartServiceTest {
    @Autowired
    private BikePartService subject;
    @MockBean
    private BikePartCategoryRepository bikePartCategoryRepository;
    @MockBean
    private BikePartRepository bikePartRepository;
    @Autowired
    private ModelMapper modelMapper;
    @BeforeEach
    void setUp() {
        PartCreateDTO partCreateDTO = mockPartCreateDTO();
        subject.savePart(partCreateDTO);
    }

    @Test
    void test_findAllPartsViewModel_returnsCorrect() {
        var partViewModel = mockPartViewModel();
        var bikePart = modelMapper.map(partViewModel,BikePart.class);

        when(bikePartRepository.findAll()).thenReturn(List.of(bikePart));

        var partViewModel1 = subject.findAllPartsViewModel().get(0); // theres only one from the setup.

        assertEquals(partViewModel.getId(), partViewModel1.getId());
        assertEquals(partViewModel.getTitle(), partViewModel1.getTitle());
        assertEquals(partViewModel.getDescription(), partViewModel1.getDescription());
        assertEquals(partViewModel.getPrice(), partViewModel1.getPrice());
        assertEquals(partViewModel.getStock(), partViewModel1.getStock());
        assertEquals(partViewModel.getCategory().getName(), partViewModel1.getCategory().getName());
        assertEquals(partViewModel.getPictureUrl(), partViewModel1.getPictureUrl());
    }

    @Test
    void test_findAllPartsByCategory_returnAllWithEmptyQuery() {
        var bikePart = new BikePart();
        var category = new BikePartCategory();
        category.setName(BikePartCategoryEnum.BRAKES);
        bikePart.setCategory(category);
        when(bikePartRepository.findAll()).thenReturn(List.of(bikePart));
        assertEquals(1, subject.findAllPartsByCategory("").size());
    }

    @Test
    void test_findAllPartsByCategory_returnNoneWithWrongQuery() {
        var bikePart = new BikePart();
        var category = new BikePartCategory();
        category.setName(BikePartCategoryEnum.BRAKES);
        bikePart.setCategory(category);
        category.setPart(Set.of(bikePart));
        when(bikePartRepository.findAll()).thenReturn(List.of(bikePart));
        assertEquals(0, subject.findAllPartsByCategory("veryTestingOfMe").size());
        assertEquals(1, subject.findAllPartsByCategory("BRAKES").size());
    }

    @Test
    void test_findAllPartsByCategory_returnOneWithGoodQuery() {
        var bikePart = new BikePart();
        var category = new BikePartCategory();
        category.setName(BikePartCategoryEnum.BRAKES);
        bikePart.setCategory(category);
        category.setPart(Set.of(bikePart));
        when(bikePartRepository.findAll()).thenReturn(List.of(bikePart));
        assertEquals(1, subject.findAllPartsByCategory("BRAKES").size());
    }

    @Test
    void test_findPartViewModelById_throwsExceptionWhenBadId() {
        when(bikePartRepository.findById(1L)).thenReturn(Optional.of(new BikePart()));
        assertNull(subject.findPartViewModelById(2L));
    }

    @Test
    void test_findPartViewModelById_returnsModelWhenGoodId() {
        var partViewModel = mockPartViewModel();
        var bikePart = modelMapper.map(partViewModel,BikePart.class);
        when(bikePartRepository.findById(1L)).thenReturn(Optional.of(bikePart));

        PartViewModel actual = subject.findPartViewModelById(1L);
        assertEquals(partViewModel.getId(), actual.getId());
        assertEquals(partViewModel.getTitle(), actual.getTitle());
        assertEquals(partViewModel.getDescription(), actual.getDescription());
        assertEquals(partViewModel.getPrice(), actual.getPrice());
        assertEquals(partViewModel.getStock(), actual.getStock());
        assertEquals(partViewModel.getCategory().getName(), actual.getCategory().getName());
        assertEquals(partViewModel.getPictureUrl(), actual.getPictureUrl());
    }

    @Test
    void test_findById_returnsNullWithBadId() {
        when(bikePartRepository.findById(1L)).thenReturn(Optional.of(new BikePart()));
        assertTrue(subject.findById(2L).isEmpty());
    }

    @Test
    void test_findById_returnsPartWithGoodId() {
        when(bikePartRepository.findById(1L)).thenReturn(Optional.of(new BikePart()));
        assertTrue(subject.findById(1L).isPresent());
    }

    @Test
    void test_deletePart_doesntDeleteWithBadId() {
        var bikePart = new BikePart();
        bikePart.setId(1L);
        when(bikePartRepository.findAll()).thenReturn(List.of(bikePart));
        subject.deletePart(2L);
        assertEquals(1, subject.findAllPartsViewModel().size());
    }

    @Test
    void test_deletePart_deletesPartWithValidId() {
        subject.deletePart(1L);
        assertEquals(0, subject.findAllPartsViewModel().size());
    }

    @Test
    void test_saveEditedPart_throwsExceptionWithBadId() {
        PartViewModel partViewModel = mockPartViewModel();

        assertThrows(NullPointerException.class,
                () -> subject.saveEditedPart(partViewModel, 2L));
    }

    @Test
    void test_saveEditedPart_savesPartWhenGivenValidId() {
        var bikePart = new BikePart();
        bikePart.setId(1L);
        var bikePart1 = bikePart;
        bikePart1.setStock(10);

        when(bikePartRepository.findById(1L)).thenReturn(Optional.of(bikePart)).thenReturn(Optional.of(bikePart1));

        var partViewModel = mockPartViewModel();
        partViewModel.setStock(10);
        subject.saveEditedPart(partViewModel, 1L);

        verify(bikePartRepository,times(2)).save(any());

        assertEquals(10, subject.findById(1L).get().getStock());
    }

    @Test
    void test_checkIfPartTitleAlreadyExists_returnsTrueWhenGivenExistingTitle() {
        var bikePart = modelMapper.map(mockPartViewModel(),BikePart.class);
        when(bikePartRepository.findByTitleIgnoreCase("title1")).thenReturn(Optional.of(bikePart));
        assertTrue(subject.checkIfPartTitleAlreadyExists("title1"));
    }

    @Test
    void test_checkIfPartTitleAlreadyExists_returnsFalseWhenGivenNewTitle() {
        var bikePart = modelMapper.map(mockPartViewModel(),BikePart.class);
        when(bikePartRepository.findByTitleIgnoreCase("title1")).thenReturn(Optional.of(bikePart));
        assertFalse(subject.checkIfPartTitleAlreadyExists("title2"));
    }

    private PartViewModel mockPartViewModel() {
        var category = new BikePartCategory();
        category.setName(BikePartCategoryEnum.BRAKES);
        var partViewModel = new PartViewModel();
        partViewModel.setId(1L);
        partViewModel.setTitle("title1");
        partViewModel.setPrice(1.0);
        partViewModel.setStock(1);
        partViewModel.setBrand("brand1");
        partViewModel.setCategory(category);
        partViewModel.setPictureUrl("pictureUrl1");
        return partViewModel;
    }

    private PartCreateDTO mockPartCreateDTO() {
        var partCreateDTO = new PartCreateDTO();
        partCreateDTO.setTitle("title1");
        partCreateDTO.setPrice(1.0);
        partCreateDTO.setStock(1);
        partCreateDTO.setBrand("brand1");
        partCreateDTO.setCategory(BikePartCategoryEnum.BRAKES);
        partCreateDTO.setPictureUrl("pictureUrl1");
        return partCreateDTO;
    }
}