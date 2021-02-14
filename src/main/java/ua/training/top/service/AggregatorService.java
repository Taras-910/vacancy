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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
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
            List<VacancyTo> ListTosForUpdate = new ArrayList<>(vacancyToForUpdate);
            Map<SubVacancyTo, VacancyTo> mapForUpdate = getMapVacancyTos(ListTosForUpdate);
            List<Vacancy> vacanciesForUpdate = new ArrayList<>();
            for (SubVacancyTo subVacancyTo : mapForUpdate.keySet()) {
                vacanciesForUpdate.add(AggregatorUtil.getForUpdate(mapForUpdate.get(subVacancyTo),
                        mapAllVacancyTos.get(subVacancyTo), parallelMap));
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
        log.info("deleteVacanciesBeforeDate reasonDateToKeep {}", reasonDateToKeep);
        List<Vacancy> listToDelete = vacanciesDb.stream()
                .filter(vacancyTo -> reasonDateToKeep.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
        if (!listToDelete.isEmpty()) {
            vacancyService.deleteList(listToDelete);
        }
    }

    public static void main(String[] args) throws IOException {
        setTestAuthorizedUser(asAdmin());
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "за_рубежем", UPGRADE));
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(asNewFreshen("java", "удаленно", UPGRADE));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());



        /*Runnable runnableTask = () -> {
            try {
                TimeUnit.HOURS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Callable<String> callableTask = () -> {
            TimeUnit.HOURS.sleep(12);
            return "Task's execution";
        };

        List<Callable<String>> callableTasks = new ArrayList<>();
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
        callableTasks.add(callableTask);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return service.scheduleAtFixedRate(command, initialDelay, period, unit);*/
    }

    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//    service.scheduleAtFixedRate(new Runnable() { ... }, 0, 1, TimeUnit.SECONDS);
//    service.scheduleWithFixedDelay(new Runnable() { ... }, 0, 1, TimeUnit.SECONDS);

    ExecutorService executorService =
            new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
}
/*executorService.execute(runnableTask);
submit() submits a Callable or a Runnable task to an ExecutorService and returns a result of type Future.

Future<String> future =
  executorService.submit(callableTask);
invokeAny() assigns a collection of tasks to an ExecutorService, causing each to be executed, and returns the result of a successful execution of one task (if there was a successful execution).

String result = executorService.invokeAny(callableTasks);
invokeAll() assigns a collection of tasks to an ExecutorService, causing each to be executed, and returns the result of all task executions in the form of a list of objects of type Future.

List<Future<String>> futures = executorService.invokeAll(callableTasks);
*/
