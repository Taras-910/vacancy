package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.AggregatorUtil;
import ua.training.top.util.EmployerUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesKeeping;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.AggregatorUtil.getAnchorEmployer;
import static ua.training.top.util.AggregatorUtil.getAnchorVacancy;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.parser.data.DataUtil.finish;

@Service
public class AggregatorService {
    private final static Logger log = LoggerFactory.getLogger(AggregatorService.class);
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private EmployerService employerService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private FreshenService freshenService;

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(freshen);

        if (!vacancyTos.isEmpty()) {
            Freshen newFreshen = freshenService.create(freshen);
            List<Vacancy>
                    vacanciesDb = vacancyService.deleteOutDatedAndGetAll(),
                    vacanciesForCreate = new ArrayList<>(),
                    vacanciesForUpdate = new ArrayList<>();
            List<Employer> employersDb = employerService.getAll();
            Map<String, Vacancy> mapVacanciesDb = vacanciesDb.stream()
                    .collect(Collectors.toMap(AggregatorUtil::getAnchorVacancy, v -> v));
            Map<String, List<Employer>> mapEmployersDb = employersDb.stream()
                    .collect(Collectors.groupingBy(AggregatorUtil::getAnchorEmployer));
            List<VacancyTo> vacancyTosByUniqueEmployers = new ArrayList<>();
            vacancyTos.forEach(vTo -> {
                if (mapEmployersDb.containsKey(getAnchorEmployer(vTo))) {
                    if (mapVacanciesDb.containsKey(getAnchorVacancy(vTo))) {
                        vacanciesForUpdate.add(getForUpdate(vTo, mapVacanciesDb.get(getAnchorVacancy(vTo))));
                    } else {
                        Vacancy v = fromTo(vTo);
                        v.setFreshen(newFreshen);
                        v.setEmployer(mapEmployersDb.get(getAnchorEmployer(vTo)).get(0));
                        vacanciesForCreate.add(v);
                    }
                } else {
                    vacancyTosByUniqueEmployers.add(vTo);
                }
            });
            Map<Employer, List<VacancyTo>> mapUniqueTos = vacancyTosByUniqueEmployers.stream()
                    .collect(Collectors.groupingBy(EmployerUtil::getEmployerFromTo));
            executeRefreshDb(mapUniqueTos, vacanciesDb, vacanciesForUpdate, vacanciesForCreate, newFreshen);
            log.info(finish, vacanciesForCreate.size(), vacanciesForUpdate.size(), freshen);
        }
    }

    @Transactional
    protected void executeRefreshDb(Map<Employer, List<VacancyTo>> mapUniqueTos, List<Vacancy> vacanciesDb,
                                    List<Vacancy> vacanciesUpdate, List<Vacancy> vacanciesCreate, Freshen newFreshen) {
        List<Employer> newEmployers = employerService.createList(new ArrayList<>(mapUniqueTos.keySet()));
        newEmployers.forEach(e -> {
            fromTos(mapUniqueTos.get(e)).forEach(v -> {
                v.setEmployer(e);
                v.setFreshen(newFreshen);
                vacanciesCreate.add(v);
            });
        });
        vacancyService.deleteExceedLimit(vacanciesDb.size() + vacanciesCreate.size() - limitVacanciesKeeping);
        Set<Vacancy> vacancies = new HashSet<>(vacanciesUpdate);
        vacancies.addAll(vacanciesCreate);
        if (!vacancies.isEmpty()) {
            vacancyService.createUpdateList(new ArrayList<>(vacancies));
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());

        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "Санкт-Петербург", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());

//        String text = "Posted on: Nov 13, 2021";
//        System.out.println(getToLocalDate(text));
    }
}

//                remote                                  foreign                         санкт-петербург

//djinni          до $3500                                  $2290-2300
//grc             до 250 000 руб.   от 60 000 руб.  100 000 – 180 000 руб.
//habr                                  от 150 000 ₽        от 150 000 до 200 000 ₽
//jobsMarket      ---------------------------
//jabs                                   від $1000           $2200–3000
//linkedin        ---------------------------
//nofluff                                                    13 000 - 26 000 PLN      // 1
//rabota          40 000 грн                                 12 000 — 20 000 грн
//indeed          ---------------------------
//joble   string1                          6 000 €           1 000 - 2 000 $           // 1
//joble   string2  //1//      Salary: 100-160 (B2B) PLN / hour. Requirements: Java, Springboot, MVC, REST APIs, microservices. Tools: Jira, GitHub, GIT, Agile, Scrum. Additionally: Sport subscription, Training budget, Private healthcare, Flat structure, Small teams, International projects, Free coffee...
//                  //1//     Salary: PLN per month: 18.0k-27.1k (B2B), 15.3k-23.0k (UoP). Requirements: Java, Spring, English, Team player, Communication skills. Tools: Jira, Confluence, Wiki, Bitbucket, Sonar, GIT, Jenkins, Agile, Scrum, SAFe. Additionally: Sport subscription, Training budget, Private...
//                       ...Senior Java Engineer Необхідні навички • 4+ years of commercial programming experience; • 4+ years of experience with Java; • Experience of developing modern REST services on Spring Boot; • Security topics hands-on with Spring Security (SAML, OAuth2); • Сommon...

//work                                                   30 000 – 90 000 грн
//yandex






