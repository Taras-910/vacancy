package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.SubVacancyTo;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.AggregatorUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesToKeep;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.AggregatorUtil.*;
import static ua.training.top.util.EmployerUtil.getEmployerMap;
import static ua.training.top.util.EmployerUtil.getEmployersFromTos;
import static ua.training.top.util.FreshenUtil.*;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.VoteUtil.getVotesOutLimitHeroku;

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
            List<VacancyTo> vacancyTosForCreate = new ArrayList<>(vacancyTos);
            List<VacancyTo> vacancyTosForUpdate = new ArrayList<>(vacancyTos);

            List<Vacancy> vacanciesDb = vacancyService.getAll();
            List<Vote> votes = voteService.getAll();
            List<VacancyTo> vacancyTosDb = getTos(vacanciesDb, votes);

            Map<VacancyTo, Vacancy> parallelMap = getParallelMap(vacanciesDb, votes);
            Map<SubVacancyTo, VacancyTo> mapAllVacancyTos = getMapVacancyTos(vacancyTosDb);
            Map<String, Employer> mapAllEmployers = getMapAllEmployers(vacancyTos);

            /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
            vacancyTosDb.forEach(vacancyTosForCreate::remove);
            vacancyTosForCreate.forEach(vacancyTosForUpdate::remove);
            List<Vacancy> vacanciesForUpdate = new ArrayList<>();
            if (!vacancyTosForUpdate.isEmpty()) {
                List<VacancyTo> ListTosForUpdate = new ArrayList<>(vacancyTosForUpdate);
                Map<SubVacancyTo, VacancyTo> mapForUpdate = getMapVacancyTos(ListTosForUpdate);
                for (SubVacancyTo subVacancyTo : mapForUpdate.keySet()) {
                    vacanciesForUpdate.add(AggregatorUtil.getForUpdate(mapForUpdate.get(subVacancyTo),
                            mapAllVacancyTos.get(subVacancyTo), parallelMap));
                }
            }
            refresh(vacancyTosForCreate, vacanciesForUpdate, freshen, mapAllEmployers, votes);
        }
    }

    @Transactional
    protected void refresh(List<VacancyTo> vacancyTosForCreate, List<Vacancy> vacanciesForUpdate,
                           Freshen freshen, Map<String, Employer> mapAllEmployers, List<Vote> votes) {
        Freshen freshenCreated = freshenService.create(freshen);
        List<Vacancy> vacanciesForCreate = getForCreate(vacancyTosForCreate, mapAllEmployers, freshenCreated);
        Set<Vacancy> vacancies = new HashSet<>(vacanciesForUpdate);
        vacancies.addAll(vacanciesForCreate);
        if (!vacancies.isEmpty()) {
            vacancyService.createUpdateList(new ArrayList<>(vacancies));
        }
        List<Vacancy> vacanciesDb = vacancyService.getAll();
        List<Freshen> freshensDb = freshenService.getAll();
        deleteOutDate(vacanciesDb, freshensDb);
        deleteLimitHeroku(vacanciesDb, freshensDb, votes);
        employerService.deleteEmptyEmployers();
        log.info("upgrade ok for Freshen: {}\n ....................................................\n", freshenCreated);
    }

    public Map<String, Employer> getMapAllEmployers(List<VacancyTo> vacancyTos){
        Set<Employer> employersDb = new HashSet<>(employerService.getAll());
        Set<Employer> employersForCreate = new HashSet<>(getEmployersFromTos(vacancyTos));
        employersDb.forEach(employersForCreate::remove);
        if (!employersForCreate.isEmpty()) {
            Set<Employer> employersNew = new HashSet<>(employerService.createList(new ArrayList<>(employersForCreate)));
            employersDb.addAll(employersNew);
        }
        return getEmployerMap(employersDb);
    }

    public void deleteOutDate(List<Vacancy> vacanciesDb, List<Freshen> freshenDb) {
        vacancyService.deleteList(getVacanciesOutPeriodToKeep(vacanciesDb));
        freshenService.deleteList(getFreshensOutPeriodToKeep(freshenDb));
    }

    public void deleteLimitHeroku(List<Vacancy> vacanciesDb, List<Freshen> freshenDb, List<Vote> votesDb) {
        vacancyService.deleteList(getVacanciesOutLimitHeroku(vacanciesDb));
        if (freshenDb.size() > limitVacanciesToKeep / 2 + 1) {
            freshenService.deleteList(getFreshensOutLimitHeroku(freshenDb));
        }
        if (votesDb.size() > limitVacanciesToKeep / 5) {
            voteService.deleteList(getVotesOutLimitHeroku(votesDb));
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "минск", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "санкт-петербург", UPGRADE));

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "intern", "foreign", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "junior", "foreign", UPGRADE));
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "foreign", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "senior", "foreign", UPGRADE));

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "intern", "remote", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "junior", "remote", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "remote", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "senior", "remote", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "expert", "remote", UPGRADE));

//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "intern", "киев", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "junior", "киев", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "киев", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "санкт-петербург", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "middle", "польша", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "senior", "киев", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "expert", "киев", UPGRADE));

        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());
    }
}
