package me.rumenblajev.bikepartshop.client;

import me.rumenblajev.bikepartshop.models.response.FreeCurrencyAPIResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class FreeCurrencyApiRestClient {
    private final WebClient webClient;

    public FreeCurrencyApiRestClient(
            @Qualifier(value = "FreeCurrencyApiRestClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public FreeCurrencyAPIResponseDTO getExchangeRates(List<String> currencies, String baseCurrency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("base_currency", baseCurrency)
                        .queryParam("currencies", String.join(",", currencies))
                        .build())
                .retrieve()
                .bodyToMono(FreeCurrencyAPIResponseDTO.class)
                .block();
    }
}
