package me.rumenblajev.bikepartshop.enums;

public enum ShoppingCurrencyEnum {
    BGN, USD, EUR;
    private double value;
    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
