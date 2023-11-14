package me.rumenblajev.bikepartshop.models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.rumenblajev.bikepartshop.enums.BikePartCategoryEnum;

@NoArgsConstructor
@Getter
@Setter
public class PartCreateDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 30, message = "Title must be between 3 and 30 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Category cannot be blank")
    private BikePartCategoryEnum category;

    @NotBlank(message = "Picture URL cannot be blank")
    private String pictureUrl;

    @NotNull(message = "Price cannot be blank")
    @Min(value = 0, message = "Price must be a positive number")
    private Double price;

    @NotBlank(message = "Brand cannot be blank")
    @Size(min = 2, max = 30, message = "Brand must be between 2 and 30 characters")
    private String brand;

    @NotNull(message = "Stock cannot be blank")
    @Min(value = 0, message = "Stock must be a positive number")
    private Integer stock;
}
