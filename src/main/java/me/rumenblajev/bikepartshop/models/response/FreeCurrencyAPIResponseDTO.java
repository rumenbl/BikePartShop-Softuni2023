package me.rumenblajev.bikepartshop.models.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
@NoArgsConstructor
@Data
public class FreeCurrencyAPIResponseDTO {
    private HashMap<String, Double> data;
}
