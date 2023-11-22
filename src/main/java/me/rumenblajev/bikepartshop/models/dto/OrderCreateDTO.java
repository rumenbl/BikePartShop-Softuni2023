package me.rumenblajev.bikepartshop.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderCreateDTO {

    @NotBlank(message = "Name cannot be blank")
    @Size(min=5, max=50, message = "Name must be between 5 and 50 characters long")
    private String clientName;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(min=8, max=20, message = "Phone number must be between 8 and 20 characters long")
    private String clientPhoneNumber;

    @NotBlank(message = "Email cannot be blank")
    @Size(min=5, max=50, message = "Email must be between 5 and 50 characters long")
    private String clientEmail;

    @NotBlank(message = "Address cannot be blank")
    @Size(min=5, max=70, message = "Address must be between 5 and 70 characters long")
    private String deliveryAddress;

    @NotBlank(message = "City cannot be blank")
    @Size(min=2,max=30, message = "City must be between 2 and 30 characters long")
    private String deliveryCity;

    @NotBlank(message = "Country cannot be blank")
    @Size(min=2,max=30, message = "Country must be between 2 and 30 characters long")
    private String deliveryCountry;

    @NotBlank(message = "Zip code cannot be blank")
    @Size(min=2,max=10, message = "Zip code must be between 2 and 10 characters long")
    private String deliveryZipCode;

    @NotBlank(message = "Courier cannot be blank")
    private String deliveryCourier;
}
