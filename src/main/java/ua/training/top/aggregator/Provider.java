package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategy.Strategy;
import ua.training.top.to.VacancyNet;

import java.io.IOException;
import java.util.List;

public class Provider {
    private static final Logger log = LoggerFactory.getLogger(Provider.class);
    private final Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<VacancyNet> getJavaVacancies(String city, String language) throws IOException {
        List<VacancyNet> list = strategy.getVacancies(city, language);
        log.info("\nstrategy {} list.size={}\n", this.strategy.getClass().getCanonicalName(), list.size());
        return list;
    }
}
