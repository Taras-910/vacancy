package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.to.DoubleWordTo;
import ua.training.top.to.VacancyNet;

import java.io.IOException;
import java.util.*;

public class AggregatorService {
    private Logger log = LoggerFactory.getLogger(AggregatorService.class);
    private Provider[] providers;
    public static ArrayDeque<Provider> allProviders;

    public AggregatorService(Provider... providers) throws IllegalArgumentException {
        this.providers = providers;
    }

    public List<VacancyNet> selectBy(DoubleWordTo task){
        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<VacancyNet> set = new HashSet<>();
        while(allProviders.peek()!=null){
            try {
                set.addAll(allProviders.pollFirst().getJavaVacancies(task));
            } catch (IOException e) {
                log.info("e {}", e.getMessage());
            }
        }
        log.info("Common number vacancies = {}", set.size());
        return new ArrayList<>(set);
    }
}
