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
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesToKeep;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodToKeep;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.AggregatorUtil.*;
import static ua.training.top.util.EmployerUtil.getEmployerMap;
import static ua.training.top.util.EmployerUtil.getEmployersFromTos;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.VacancyUtil.getTos;

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
            List<VacancyTo> vacancyToForUpdate = new ArrayList<>(vacancyTos);
            List<Vacancy> vacanciesDb = vacancyService.getAll();
            List<Vote> votes = voteService.getAll();
            List<VacancyTo> vacancyTosDb = getTos(vacanciesDb, votes);
            Map<VacancyTo, Vacancy> parallelMap = getParallelMap(vacanciesDb, votes);
            Map<SubVacancyTo, VacancyTo> mapAllVacancyTos = getMapVacancyTos(vacancyTosDb);
            Map<String, Employer> mapAllEmployers = getMapAllEmployers(vacancyTos);

            /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
            vacancyTosDb.forEach(vacancyTosForCreate::remove);
            vacancyTosForCreate.forEach(vacancyToForUpdate::remove);
            List<Vacancy> vacanciesForUpdate = new ArrayList<>();
            if (!vacancyToForUpdate.isEmpty()) {
                List<VacancyTo> ListTosForUpdate = new ArrayList<>(vacancyToForUpdate);
                Map<SubVacancyTo, VacancyTo> mapForUpdate = getMapVacancyTos(ListTosForUpdate);
                for (SubVacancyTo subVacancyTo : mapForUpdate.keySet()) {
                    vacanciesForUpdate.add(AggregatorUtil.getForUpdate(mapForUpdate.get(subVacancyTo),
                            mapAllVacancyTos.get(subVacancyTo), parallelMap));
                }
            }
            refresh(vacancyTosForCreate, vacanciesForUpdate, freshen, mapAllEmployers, vacanciesDb);
        }
    }

    @Transactional
    protected void refresh(List<VacancyTo> vacancyTosForCreate, List<Vacancy> vacanciesForUpdate,
                           Freshen freshen, Map<String, Employer> mapAllEmployers, List<Vacancy> vacanciesDb) {
        Freshen freshenDb = freshenService.create(freshen);
        List<Vacancy> vacanciesForCreate = getForCreate(vacancyTosForCreate, mapAllEmployers, freshenDb);
        Set<Vacancy> vacancies = new HashSet<>(vacanciesForUpdate);
        vacancies.addAll(vacanciesForCreate);
        if (!vacancies.isEmpty()) {
            vacancyService.createUpdateList(new ArrayList<>(vacancies));
        }
        deleteVacanciesOutdated(vacanciesDb, reasonPeriodToKeep);
        deleteVacanciesOutLimited(limitVacanciesToKeep);
        deleteFreshensOutLimit(limitVacanciesToKeep / 7);
        employerService.deleteEmptyEmployers();
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

    @Transactional
    public void deleteVacanciesOutdated(List<Vacancy> vacanciesDb, LocalDate reasonDateToKeep) {
        log.info("deleteVacanciesBeforeDate reasonDateToKeep={}", reasonDateToKeep);
        List<Vacancy> listToDelete = vacanciesDb.parallelStream()
                .filter(vacancyTo -> reasonDateToKeep.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
        if (!listToDelete.isEmpty()) {
            log.info("deleteList {}", listToDelete.size());
            vacancyService.deleteList(listToDelete);
        }
    }

    @Transactional
    public void deleteFreshensOutLimit(int limitFreshenToKeep) {
        log.info("deleteFreshensOutLimit limitFreshenToKeep={}", limitFreshenToKeep);
        List<Freshen> allFreshens = freshenService.getAll();
        if (allFreshens.size() >= limitFreshenToKeep) {
        List<Freshen> listToDelete = allFreshens.parallelStream()
                .sorted((f1, f2) -> f1.getRecordedDate().isAfter(f2.getRecordedDate()) ? 1 : 0)
                .skip(limitFreshenToKeep)
                .collect(Collectors.toList());
        freshenService.deleteList(listToDelete);
        }
    }

    public void deleteVacanciesOutLimited(int limitVacanciesToKeep) {
        List<Vacancy> vacancies = vacancyService.getAll();
        log.info("deleteVacanciesOutLimited limitVacanciesToKeep={} vacancies={}", limitVacanciesToKeep, vacancies.size());
        List<Vacancy> listToDelete = Optional.of(vacancies.parallelStream()
                .sorted((v1, v2) -> v1.getReleaseDate().isAfter(v2.getReleaseDate()) ? 1 : 0)
                .sequential()
                .skip(limitVacanciesToKeep)
                .collect(Collectors.toList())).orElse(new ArrayList<>());
        if (!listToDelete.isEmpty()) {
            log.info("deleteList={}", listToDelete.size());
            vacancyService.deleteList(listToDelete);
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());
//        setTestProvider();
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "санкт-петербург", UPGRADE));
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "за_рубежем", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "удаленно", UPGRADE));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "киев", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());
//        offTestProvider();
    }
}
