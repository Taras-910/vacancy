package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategy.Strategy;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reasonToLoadDate;

public class Provider {
    private static final Logger log = LoggerFactory.getLogger(Provider.class);
    private final Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<VacancyTo> getJavaVacancies(DoubleString doubleString) throws IOException {
        List<VacancyTo> list = strategy.getVacancies(doubleString);
        log.info("\nstrategy {} list.size={}\n", this.strategy.getClass().getCanonicalName(), list.size());
        return list.stream()
                .filter(vacancyTo -> !reasonToLoadDate.isAfter(vacancyTo.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
                .collect(Collectors.toList());
    }
}
