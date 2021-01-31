package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.*;
import ua.training.top.service.EmployerService;
import ua.training.top.service.FreshenService;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reasonPeriodToKeep;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.util.VacancyUtil.*;

@Controller
public class AggregatorController {
    private final static Logger log = LoggerFactory.getLogger(AggregatorController.class);
    private final VacancyService vacancyService;
    private final EmployerService employerService;
    private final VoteService voteService;
    private final FreshenService freshenService;


    public AggregatorController(VacancyService vacancyService, EmployerService employerService,
                                VoteService voteService, FreshenService freshenService) {
        this.vacancyService = vacancyService;
        this.employerService = employerService;
        this.voteService = voteService;
        this.freshenService = freshenService;
    }

    public void refreshDB(Freshen freshen) {
        log.info("refreshDB by freshen {}", freshen);
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(freshen);

        List<VacancyTo> vacancyToForCreate = new ArrayList<>(vacancyTos);
        List<VacancyTo> vacancyToForUpdate = new ArrayList<>(vacancyTos);
        List<Vacancy> vacanciesDb = vacancyService.getAll();
        List<Vote> votes = voteService.getAll();
        List<VacancyTo> vacancyTosDb = getTos(vacanciesDb, votes);
        Map<VacancyTo, Vacancy> parallelMap = getParallelMap(vacanciesDb, votes);
        Map<SubVacancyTo, VacancyTo> mapAll = getMapVacancyTos(vacancyTosDb);

        /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
        vacancyTosDb.forEach(i -> vacancyToForCreate.remove(i));
        vacancyToForCreate.forEach(i -> vacancyToForUpdate.remove(i));
        List<VacancyTo> ListTosForUpdate = new ArrayList<>(vacancyToForUpdate);
        Map<SubVacancyTo, VacancyTo> mapForUpdate = getMapVacancyTos(ListTosForUpdate);
        List<Vacancy> resultForSave = new ArrayList<>();
        for (SubVacancyTo vst : mapForUpdate.keySet()) {
            resultForSave.add(populateVacancy(mapForUpdate.get(vst), mapAll.get(vst), parallelMap));
        }
        refreshDb(vacancyToForCreate, resultForSave, freshen);
    }

    @Transactional
    protected void refreshDb(List<VacancyTo> vacancyToForCreate, List<Vacancy> resultForSave, Freshen freshen) {
        Freshen freshenDb = freshenService.create(freshen);
        if (!vacancyToForCreate.isEmpty()) {
            vacancyService.createListVacancyAndEmployer(vacancyToForCreate, freshenDb);
        }
        if (!resultForSave.isEmpty()) {
            vacancyService.createUpdateList(resultForSave);
        }
        deleteVacanciesOutdated(reasonPeriodToKeep);
        employerService.deleteEmptyEmployers();
    }

    @Transactional
    protected void deleteVacanciesOutdated(LocalDate reasonDateToKeep) {
        log.info("deleteVacanciesBeforeDate reasonDateToKeep {}", reasonDateToKeep);
        List<Vacancy> listToDelete = vacancyService.getAll().stream()
                .filter(vacancyTo -> reasonDateToKeep.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
        vacancyService.deleteList(listToDelete);
    }

    public static void main(String[] args) throws IOException {
        User admin = new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        setTestAuthorizedUser(admin);
        String language = "java";
        String workplace = "киев";
//        String workplace = "за_рубежем";
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new Freshen(null, LocalDateTime.now(), language, workplace, authUserId()));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());
    }
}
