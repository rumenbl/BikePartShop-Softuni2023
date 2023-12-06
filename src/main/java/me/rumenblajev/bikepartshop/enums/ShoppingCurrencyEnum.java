package me.rumenblajev.bikepartshop.enums;

import lombok.Getter;
import lombok.Setter;

@Getter
public enum ShoppingCurrencyEnum {
    BGN, USD, EUR;
    @Setter
    private double value;
}
