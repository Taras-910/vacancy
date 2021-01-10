package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AggregatorService {
    private Logger log = LoggerFactory.getLogger(AggregatorService.class);
    private Provider[] providers;
    public static ArrayDeque<Provider> allProviders;

    public AggregatorService(Provider... providers) throws IllegalArgumentException {
        this.providers = providers;
    }

    public List<VacancyTo> selectBy(Freshen freshen){
        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<VacancyTo> set = new HashSet<>();
        while(allProviders.peek()!=null){
            try {
                set.addAll(allProviders.pollFirst().getJavaVacancies(freshen));
            } catch (IOException e) {
                log.error("e {}", e.getMessage());
            }
        }
        log.info("Common number vacancies = {}", set.size());
        return set.stream()
                .map(vacancyTo -> {
                    vacancyTo.setId(null);
                    vacancyTo.setWorkplace(freshen.getWorkplace());
                    vacancyTo.setLanguage(freshen.getLanguage());
                    vacancyTo.setToVote(false);
                    return vacancyTo; })
                .collect(Collectors.toList());
    }
}
