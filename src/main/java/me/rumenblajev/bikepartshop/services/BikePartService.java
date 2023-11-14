package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.models.entity.BikePart;
import me.rumenblajev.bikepartshop.models.entity.BikePartCategory;
import me.rumenblajev.bikepartshop.models.view.PartViewModel;
import me.rumenblajev.bikepartshop.repositories.BikePartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BikePartService {
    private final BikePartRepository bikePartRepository;
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

}
