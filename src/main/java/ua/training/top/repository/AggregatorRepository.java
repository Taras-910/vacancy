package ua.training.top.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.aggregator.Provider;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.ValidationUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reasonDateToLoad;
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
        List<VacancyTo> vacancyTos= set.stream()
                .filter(ValidationUtil::checkNullDataVacancyTo)
                .filter(v -> reasonDateToLoad.isBefore(v.getReleaseDate()))
                .filter(v -> (v.getTitle().toLowerCase().contains(freshen.getLanguage())
                        || v.getSkills().toLowerCase().contains(freshen.getLanguage())))
                .map(vacancyTo -> getFull(vacancyTo, freshen))
                .distinct()
                .collect(Collectors.toList());
        log.info("Common number vacancies = {}", vacancyTos.size());
        return vacancyTos;
    }
}
