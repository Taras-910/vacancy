package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.Profiles;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.EmployerUtil;
import ua.training.top.util.aggregatorUtil.data.ToUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.Dispatcher.getAllProviders;
import static ua.training.top.aggregator.InstallationUtil.limitVacanciesKeeping;
import static ua.training.top.util.MessageUtil.finish_message;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ToUtil.getAnchorEmployer;
import static ua.training.top.util.aggregatorUtil.data.ToUtil.getAnchorVacancy;

@Service
@EnableScheduling
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    public static boolean herokuRestriction;

    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private EmployerService employerService;
    @Autowired
    private FreshenService freshenService;
    @Autowired
    private RateService rateService;

    @Autowired
    @SuppressWarnings("deprecation")
    public void setEnvironment(Environment environment) {
        herokuRestriction = environment.acceptsProfiles(Profiles.HEROKU);
    }

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        boolean isRateOld = rateService.CurrencyRatesMapInit();
        Instant start = Instant.now();
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(freshen);
        Freshen newFreshen = freshenService.create(freshen);
        if (!vacancyTos.isEmpty()) {
            List<Vacancy>
                    vacanciesDb = vacancyService.getAll(),
                    vacanciesCreate = new ArrayList<>(),
                    vacanciesUpdate = new ArrayList<>();
            List<Employer> employersDb = employerService.getAll();
            Map<String, Vacancy> mapVacanciesDb = vacanciesDb.stream()
                    .collect(Collectors.toMap(ToUtil::getAnchorVacancy, v -> v));
            Map<String, List<Employer>> mapEmployersDb = employersDb.stream()
                    .collect(Collectors.groupingBy(ToUtil::getAnchorEmployer));
            List<VacancyTo> vacancyTosOfUniqueEmployers = new ArrayList<>();
            vacancyTos.forEach(vTo -> {
                if (mapEmployersDb.containsKey(getAnchorEmployer(vTo))) {
                    if (mapVacanciesDb.containsKey(getAnchorVacancy(vTo))) {
                        vacanciesUpdate.add(getForUpdate(vTo, mapVacanciesDb.get(getAnchorVacancy(vTo))));
                    } else {
                        Vacancy v = fromTo(vTo);
                        v.setFreshen(newFreshen);
                        v.setEmployer(mapEmployersDb.get(getAnchorEmployer(vTo)).get(0));
                        vacanciesCreate.add(v);
                    }
                } else {
                    vacancyTosOfUniqueEmployers.add(vTo);
                }
            });
            Map<Employer, List<VacancyTo>> mapUniqueTos = vacancyTosOfUniqueEmployers.stream()
                    .collect(Collectors.groupingBy(EmployerUtil::getEmployerFromTo));
            executeRefreshDb(mapUniqueTos, vacanciesDb, vacanciesUpdate, vacanciesCreate, newFreshen);
            long timeElapsed = Duration.between(start, Instant.now()).toMillis();
            log.info(finish_message, timeElapsed, vacanciesCreate.size(), vacanciesUpdate.size(),
                    newFreshen.getLanguage(), newFreshen.getLevel(), newFreshen.getWorkplace());
            if (isRateOld) {
                updateRateDB();
            }
        }
    }

    @Transactional
    protected void executeRefreshDb(Map<Employer, List<VacancyTo>> mapUniqueTos, List<Vacancy> vacanciesDb,
                                    List<Vacancy> vacanciesUpdate, List<Vacancy> vacanciesCreate, Freshen newFreshen) {
        log.info("executeRefreshDb");
        List<Employer> newEmployers = employerService.createList(new ArrayList<>(mapUniqueTos.keySet()));
        newEmployers.forEach(e -> fromTos(mapUniqueTos.get(e)).stream().distinct().forEach(v -> {
            v.setEmployer(e);
            v.setFreshen(newFreshen);
            vacanciesCreate.add(v);
        }));
        Set<Vacancy> vacancies = new HashSet<>(vacanciesUpdate);
        vacancies.addAll(vacanciesCreate);
        if (!vacancies.isEmpty()) {
            vacancyService.deleteExceed(vacanciesDb.size() + vacanciesCreate.size() - limitVacanciesKeeping);
            vacancyService.createUpdateList(new ArrayList<>(vacancies));
            employerService.deleteEmpty(vacancyService.getAll());
        }
    }

    public void deleteOutDated() {
        log.info("deleteOutDated");
        vacancyService.deleteOutDated();
        freshenService.deleteOutDated();
    }

    public void updateRateDB() {
        log.info("updateRateDB");
        rateService.updateRateDB();
    }

    public static void main(String[] args) {
        setTestAuthorizedUser(asAdmin());
        /*rate*/
        /*AtomicInteger i = new AtomicInteger(1);
        List<Rate> rates = getRateProvider().getRates(baseCurrency);
        rates.forEach(rate -> log.info("\nrate № {}\n{}\n", i.getAndIncrement(), rate.toString()));
        log.info("\n\ncommon = {}", rates.size());*/

        /*List<VacancyTo> vacancyTos = getAllProviders().selectBy(
                FreshenUtil.asNewFreshen("all", "all", "all", Goal.UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());*/


    }
}
