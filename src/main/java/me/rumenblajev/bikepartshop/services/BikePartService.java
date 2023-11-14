package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;
import me.rumenblajev.bikepartshop.models.dto.PartCreateDTO;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.BikePartCategory;
import me.rumenblajev.bikepartshop.models.entity.Brand;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.repositories.BikePartCategoryRepository;
import me.rumenblajev.bikepartshop.repositories.BikePartRepository;
import me.rumenblajev.bikepartshop.repositories.BrandRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BikePartService {
    private final BikePartRepository bikePartRepository;
    private final BrandRepository brandRepository;
    private final BikePartCategoryRepository bikePartCategoryRepository;
    private final ModelMapper modelMapper;
    public List<PartViewModel> findAllPartsViewModel() {
        return bikePartRepository.findAll().stream()
                .map(part -> modelMapper.map(part, PartViewModel.class))
                .toList();
    }

    public List<PartViewModel> findAllPartsByCategory(final String query) {
        return bikePartRepository.findAll().stream().filter(part -> part.getCategory().getName().toString().toLowerCase().contains(query.toLowerCase()))
                .map(part -> modelMapper.map(part, PartViewModel.class))
                .toList();
    }
    public PartViewModel findPartViewModelById(Long id) {
        return modelMapper.map(findById(id), PartViewModel.class);
    }
    public BikePart findById(Long id) {
        return bikePartRepository.findById(id).orElse(null);
    }

    public void deletePart(Long id) {
        bikePartRepository.deleteById(id);
    }

    public void saveEditedPart(PartViewModel partViewModel, Long id) {
        BikePart bikePart = bikePartRepository.findById(id).orElse(null);
        bikePart.setPrice(partViewModel.getPrice());
        bikePart.setStock(partViewModel.getStock());
        bikePartRepository.save(bikePart);
    }

    public boolean checkIfPartTitleAlreadyExists(String title) {
        return bikePartRepository.findByTitleIgnoreCase(title).isPresent();
    }

    public void savePart(PartCreateDTO partCreateDTO) {
        BikePart bikePart = modelMapper.map(partCreateDTO, BikePart.class);
        Brand brand = brandRepository.findByBrandNameIgnoreCase(partCreateDTO.getBrand()).orElse(null);

        if(brand == null) {
            Brand brandToSave = new Brand();
            brandToSave.setBrandName(partCreateDTO.getBrand());
            brandRepository.save(brandToSave);

        }
        bikePart.setBrand(brandRepository.findByBrandNameIgnoreCase(partCreateDTO.getBrand()).orElse(null));
        BikePartCategory category = bikePartCategoryRepository.findByName(partCreateDTO.getCategory()).orElse(null);
        bikePart.setCategory(category);
        bikePartRepository.save(bikePart);
    }
}
