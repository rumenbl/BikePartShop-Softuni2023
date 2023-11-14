package me.rumenblajev.bikepartshop.models.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.models.entity.BikePartCategory;

@NoArgsConstructor
@Getter
@Setter
public class PartViewModel {
    private Long id;
    private String title;
    private String description;
    private String pictureUrl;
    private Double price;
    private Integer stock;
    private BikePartCategory category;
    private String brand;
}
