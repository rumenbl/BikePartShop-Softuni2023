package me.rumenblajev.bikepartshop.schedulers;

import lombok.RequiredArgsConstructor;
import me.rumenblajev.bikepartshop.services.ExchangeRateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class ExchangeRateScheduler {
    private final ExchangeRateService exchangeRateService;
    @Scheduled(fixedRateString = "${currency.update.interval}", timeUnit = TimeUnit.SECONDS)
    private void updateExchangeRates() {
        exchangeRateService.updateAllExchangeRates();
    }
}
