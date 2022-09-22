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
import ua.training.top.util.collect.data.ToUtil;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.Dispatcher.getAllProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesKeeping;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.collect.data.DataUtil.finish_message;
import static ua.training.top.util.collect.data.ToUtil.getAnchorEmployer;
import static ua.training.top.util.collect.data.ToUtil.getAnchorVacancy;

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
    @SuppressWarnings("deprecation")
    public void setEnvironment(Environment environment) {
        System.out.println("=".repeat(100));
        System.out.println(Arrays.toString(environment.getDefaultProfiles()));
        System.out.println(Arrays.toString(environment.getActiveProfiles()));
        System.out.println("+".repeat(100));

        herokuRestriction = environment.acceptsProfiles(Profiles.HEROKU);
    }

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        Instant start = Instant.now();

        List<VacancyTo> vacancyTos = getAllProviders().selectBy(freshen);

        if (!vacancyTos.isEmpty()) {
            Freshen newFreshen = freshenService.create(freshen);
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
            log.info(finish_message, timeElapsed, vacanciesCreate.size(), vacanciesUpdate.size(), newFreshen);
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

    public static void main(String[] args) {
        setTestAuthorizedUser(asAdmin());

        List<VacancyTo> vacancyTos = getAllProviders().selectBy(
                asNewFreshen("all", "all", "london", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());

/*
        String salaryString = "London - Lynx Recruitment Ltd Salary: GBP 45,000 - 55,000 Annual Posted: 2 hours ago";
//        String titleString = "1st Executive Limited Salary: $60,000 Posted: 1 hour ago";
        System.out.println(Arrays.toString(getToSalaries(salaryString)));
        System.out.println(Arrays.toString(getToSalaries(getSalaryFromText(salaryString))));
*/
    }


}
//dateString     =yesterday
//date           =2022-09-08
//                                   *      *
//  djinni*12 grc*20 habr*25 jobMar jobs linked nof rab*40 indeed joble work jobcareer total
//all     100   40     20    10     14   2х14    4    25   25*20 10*20   27    2        291
//remote  100   33     13    10      1      3    4     9     7    13x20   9    -        202
//Киев    100    4     20     -      1      2    -    12    25    22     13    2        201
//foreign 120    1      1    10     14   2х14    4     1     -    1*14*   3    -        169
//Минск   100    6     20     -      1      2    -     1     -     2      3    2        137
//Украина   6    6      -     -     14   2х14    -     6    25    2*14*  27    2        128
//Харьков  46    2      -     -      1      2    -     4          11      5    1         72
//Львов    40    -      -     -      1      2    -     3          20      3    1         70
//Москва    -   40     20     -      1      3    -     -     -     -      -    3         67
//Санкт-Петербург20    20     -      1      3    -     -     -     -      -    4         48
//Одесса   30    -      -     -      1      2    -     2     2     6      3    1         47
//                              trainee=164


