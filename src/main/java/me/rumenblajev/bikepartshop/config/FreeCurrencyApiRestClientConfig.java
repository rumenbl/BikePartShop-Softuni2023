package me.rumenblajev.bikepartshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FreeCurrencyApiRestClientConfig {
    @Value("${currency.update.api-key}")
    private String apiKey;
    private static final String FREE_CURRENCY_API_BASE_URL = "https://api.freecurrencyapi.com/v1/latest";
    private static final String FREE_CURRENCY_API_KEY_QUERY_PARAM = "?apikey=";
    @Bean(name = "FreeCurrencyApiRestClient")
    public WebClient getWebClient(final WebClient.Builder builder) {
        return builder
                .baseUrl(FREE_CURRENCY_API_BASE_URL + FREE_CURRENCY_API_KEY_QUERY_PARAM + apiKey)
                .build();
    }
}
