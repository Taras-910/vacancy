package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import java.io.IOException;
import java.util.*;

import static ua.training.top.aggregator.InstallationUtil.reasonDateLoading;
import static ua.training.top.util.MessageUtil.common_number_vacancyTos;
import static ua.training.top.util.MessageUtil.error_select;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.isMatch;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.citiesRU;
import static ua.training.top.util.aggregatorUtil.data.ToUtil.getFilled;

@Repository
public class Starter implements StarterInterface {
    private final Logger log = LoggerFactory.getLogger(Starter.class);

    private final Provider[] providers;
    public static ArrayDeque<Provider> allProviders;

    public Starter(Provider... providers) throws IllegalArgumentException {
        this.providers = providers;
    }

    @Override
    public List<VacancyTo> selectBy(Freshen freshen){
        if(isMatch(citiesRU, freshen.getWorkplace())){
            return new ArrayList<>();
        }

        allProviders = new ArrayDeque<>(Arrays.asList(providers));
        Set<VacancyTo> set = new HashSet<>();
        while(allProviders.peek()!=null){
            try {
                set.addAll(allProviders.pollFirst().getVacancies(freshen));
            } catch (IOException e) {
                log.error(error_select, e.getMessage());
            }
        }
        List<VacancyTo> vacancyTos = set.parallelStream()
                .filter(VacancyUtil::checkNullDataVacancyTo)
                .filter(vTo -> reasonDateLoading.isBefore(vTo.getReleaseDate()))
                .map(vTo -> getFilled(vTo, freshen)).distinct().toList();
        log.info(common_number_vacancyTos, vacancyTos.size());
        return vacancyTos;
    }
}
