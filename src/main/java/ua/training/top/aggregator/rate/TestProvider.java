package ua.training.top.aggregator.rate;

import ua.training.top.model.Rate;

import java.time.LocalDate;
import java.util.List;

public class TestProvider implements RateProvider{
    public List<Rate> getRates(String baseCurrency) {
        return List.of(new Rate(null,  "USDEUR", 1.02021, LocalDate.of(2020,10,25)),
                new Rate(null,  "USDGBP", 0.89565, LocalDate.of(2020,10,25)));
    }

}
