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
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.Dispatcher.getAllProviders;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesKeeping;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.collect.data.ConstantsUtil.finish_message;
import static ua.training.top.util.collect.data.SalaryUtil.getToSalaries;
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

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(
//                asNewFreshen("java", "all", "all", UPGRADE));
//        AtomicInteger i = new AtomicInteger(1);
//        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
//        log.info("\n\ncommon = {}", vacancyTos.size());

        String salary = "$130,000 - $140,000 / year";
        System.out.println(getToSalaries(salary));

    }
}



//   у.е. work
//                    /*new Provider(new CaIndeedStrategy()),*/   /*только ca*/
//                    /*new Provider(new CwJobsStrategy())*/      /*только uk*/                // не работает!!!!!!!!!!
//                    new Provider(new DjinniStrategy()),       /*за_рубежем === удаленно*/
//                    new Provider(new ItJobsStrategy()),       /*только ca*/
//                    new Provider(new ItJobsWatchStrategy()),  /*только uk*/

//                    new Provider(new JobBankStrategy()),      /*только ca - от правительства канады*/
//                    new Provider(new JobsBGStrategy()),       /*только bg*/

//                    new Provider(new JobsMarketStrategy()),   /*только за_рубежем USA!!!*/
// salary???   on site: $130,000 - $140,000 / year


//                    new Provider(new JobsDouStrategy()),      /*полезные статьи  */
//                    new Provider(new LinkedinStrategy()),     /*нет удаленно  нет salary*/
//                    new Provider(new NofluffjobsStrategy()),  /*только pl*/
//                    /*new Provider(new RabotaStrategy()),*/     /*мало за_рубежем Украина ??? страница: js-функция */     //!!!!!!!!!!
//                    /*new Provider(new ReedStrategy())*/        /*только uk*/             // не работает!!!!!!!!!!
//                    /*new Provider(new UAIndeedStrategy()),*/   /*только ua // нет salary*/
//                    new Provider(new UAJoobleStrategy()),     /*меняет теги //ua, bg, ca, uk, de */
//                    new Provider(new WorkStrategy()),         /*нет за_рубежем*/
//                    new Provider(new ZaplataStrategy())       /*только bg*/


//                                   *      *
//   djinni*12 jobMar jobs linked nof rab*40 indeed joble work jobcareer total
//all     100   10     14   2х14    4    25   25*20 10*20   27    2        291
//remote  100   10      1      3    4     9     7    13x20   9    -        202
//Киев    100    -      1      2    -    12    25    22     13    2        201
//foreign 120   10     14   2х14    4     1     -    1*14*   3    -        169
//Минск   100    -      1      2    -     1     -     2      3    2        137
//Украина   6    -     14   2х14    -     6    25    2*14*  27    2        128
//Харьков  46    -      1      2    -     4          11      5    1         72
//Львов    40    -      1      2    -     3          20      3    1         70
//Одесса   30    -      1      2    -     2     2     6      3    1         47
//                trainee=164


