package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

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
        return bikePartRepository.findAll().stream().filter(
                    part -> part.getCategory().getName().toString().toLowerCase().contains(query.toLowerCase())
                )
                .map(part -> modelMapper.map(part, PartViewModel.class))
                .toList();
    }
    public PartViewModel findPartViewModelById(final Long id) {
        return modelMapper.map(findById(id), PartViewModel.class);
    }
    public Optional<BikePart> findById(final Long id) {
        return bikePartRepository.findById(id);
    }

    public void deletePart(final Long id) {
        bikePartRepository.deleteById(id);
    }

    public void saveEditedPart(final PartViewModel partViewModel, final Long id) {
        BikePart bikePart = bikePartRepository.findById(id).orElse(null);
        bikePart.setPrice(partViewModel.getPrice());
        bikePart.setStock(partViewModel.getStock());
        bikePartRepository.save(bikePart);
    }

    public boolean checkIfPartTitleAlreadyExists(final String title) {
        return bikePartRepository.findByTitleIgnoreCase(title).isPresent();
    }

    public void savePart(final PartCreateDTO partCreateDTO) {
        final var bikePart = modelMapper.map(partCreateDTO, BikePart.class);
        final var brand = brandRepository.findByBrandNameIgnoreCase(partCreateDTO.getBrand());

        if(brand.isEmpty()) {
            final var brandToSave = new Brand();
            brandToSave.setBrandName(partCreateDTO.getBrand());
            brandRepository.save(brandToSave);

        }
        bikePart.setBrand(brandRepository.findByBrandNameIgnoreCase(partCreateDTO.getBrand()).orElse(null));
        BikePartCategory category = bikePartCategoryRepository.findByName(partCreateDTO.getCategory()).orElse(null);
        bikePart.setCategory(category);
        bikePartRepository.save(bikePart);
    }
}
