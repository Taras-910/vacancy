package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategy.Strategy;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static ua.training.top.util.MessageUtil.number_inform;

public class Provider {
    private static final Logger log = LoggerFactory.getLogger(Provider.class);
    private final Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Instant start = Instant.now();
        List<VacancyTo> list = strategy.getVacancies(freshen);
        long timeElapsed = Duration.between(start, Instant.now()).toMillis();
        log.info(number_inform, this.strategy.getClass().getSimpleName(), timeElapsed, list.size());
        return list;
    }





}
