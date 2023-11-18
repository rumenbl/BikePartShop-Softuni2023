package me.rumenblajev.bikepartshop.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderCreateDTO {
    private String clientName;
    private String clientPhoneNumber;
    private String clientEmail;
    private String deliveryAddress;
    private String deliveryCity;
    private String deliveryCountry;
    private String deliveryZipCode;
    private String deliveryCourier;
}
