package me.rumenblajev.bikepartshop.services;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.client.FreeCurrencyApiRestClient;
import me.rumenblajev.bikepartshop.enums.ShoppingCurrencyEnum;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {
    private final FreeCurrencyApiRestClient freeCurrencyApiRestClient;
    public void updateAllExchangeRates() {
        final List<String> currenciesList = Arrays.stream(ShoppingCurrencyEnum.values())
                .map(Enum::toString)
                .toList();

        final var apiResponse = freeCurrencyApiRestClient.getExchangeRates(currenciesList, "BGN");

        apiResponse.getData().forEach(this::updateExchangeRate);
    }
    public void updateExchangeRate(final String currency,final double newValue) {
        ShoppingCurrencyEnum.valueOf(currency).setValue(newValue);
    }
}
