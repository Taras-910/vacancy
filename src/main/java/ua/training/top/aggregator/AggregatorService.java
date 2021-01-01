package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.to.DoubleString;
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

    public List<VacancyTo> selectBy(DoubleString doubleString){
        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<VacancyTo> set = new HashSet<>();
        while(allProviders.peek()!=null){
            try {
                set.addAll(allProviders.pollFirst().getJavaVacancies(doubleString));
            } catch (IOException e) {
                log.info("e {}", e.getMessage());
            }
        }
        log.info("Common number vacancies = {}", set.size());
        return set.stream()
                .map(vacancyTo -> {
                    vacancyTo.setId(null);
                    vacancyTo.setWorkPlace(doubleString.getWorkplaceTask());
                    vacancyTo.setLanguage(doubleString.getLanguageTask());
                    vacancyTo.setToVote(false);
                    return vacancyTo; })
                .collect(Collectors.toList());
    }
}
