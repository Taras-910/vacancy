package ua.training.top.service;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodToKeep;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.xss.XssUtil.xssClear;

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
            refresh(vacancyToForCreate, resultForSave, freshen);
        }
    }

    @Transactional
    protected void refresh(List<VacancyTo> vacancyToForCreate, List<Vacancy> resultForSave, Freshen freshen) {
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
/*
        User admin = new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        setTestAuthorizedUser(admin);
        String language = "java";
        String workplace = "киев";
//        String workplace = "за_рубежем";
        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new Freshen(null, LocalDateTime.now(), language, workplace, authUserId()));
        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());
*/
        String text = "Admin<script>alert('XSS')</script>";
        System.out.println(xssClear(text));
        String safeText = Jsoup.clean(text, Whitelist.basic());
        System.out.println(safeText);

/*
                String unsafe =
                "<p><a href='http://example.com/' onclick='stealCookies()'>Link</a></p>";
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        // now: <p><a href="http://example.com/" rel="nofollow">Link</a></p>
        System.out.println(safe);
*/
    }
}
