package ua.training.top.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.aggregator.Provider;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.getFull;

@Repository
public class AggregatorRepository implements AggregatorInterface{
    private Logger log = LoggerFactory.getLogger(AggregatorRepository.class);

    private Provider[] providers;
    public static ArrayDeque<Provider> allProviders;

    public AggregatorRepository(Provider... providers) throws IllegalArgumentException {
        this.providers = providers;
    }

    @Override
    public List<VacancyTo> selectBy(Freshen freshen){
        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<VacancyTo> set = new HashSet<>();
        while(allProviders.peek()!=null){
            try {
                set.addAll(allProviders.pollFirst().getJavaVacancies(freshen));
            } catch (IOException e) {
                log.error("selectBy e {}", e.getMessage());
            }
        }
        log.info("Common number vacancies = {}", set.size());
        return set.stream()
                .map(vacancyTo -> getFull(vacancyTo, freshen))
                .collect(Collectors.toList());
    }
}
