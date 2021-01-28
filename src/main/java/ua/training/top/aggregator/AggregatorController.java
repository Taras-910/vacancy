package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.*;
import ua.training.top.service.EmployerService;
import ua.training.top.service.FreshenService;
import ua.training.top.service.VacancyService;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.strategy.provider.ProviderUtil.getAllProviders;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.VacancyUtil.fromTo;
import static ua.training.top.util.refresh.VacanciesMapUtil.getMapVacanciesForCreate;
import static ua.training.top.util.refresh.VacanciesMapUtil.getMapVacanciesForUpdate;

@Controller
public class AggregatorController {
    private final static Logger log = LoggerFactory.getLogger(AggregatorController.class);
    private final VacancyService vacancyService;
    private final EmployerService employerService;
    private final FreshenService freshenService;

    public AggregatorController(VacancyService vacancyService,
                                EmployerService employerService, FreshenService freshenService) {
        this.vacancyService = vacancyService;
        this.employerService = employerService;
        this.freshenService = freshenService;
    }

    public void refreshDB(Freshen freshen){
        log.info("refreshDB by doubleString {}", freshen);

        List<VacancyTo> vacancyTos = getAllProviders().selectBy(freshen);

        List<Employer> employersDb = employerService.getAll();
        List<Vacancy> vacanciesDb = vacancyService.getByFilter(freshen);

//        deleteOutdated(reasonDateToKeep);
        Set<Employer> employersForCreate = new HashSet();
        Set<Employer> employersForUpdate = new HashSet();
        Set<Vacancy> vacanciesForUpdate = new HashSet<>();
        Set<VacancyTo> tosExistEmployers = new HashSet();

        vacancyTos.forEach(v_To -> {
            AtomicBoolean unDouble = new AtomicBoolean(true);
            Vacancy vacancyFromTo = fromTo(v_To);
            List<Employer> tempEmployersForUpdate = null;
            List<Employer> tempEmployersForCreate = null;
            List<Vacancy> tempVacanciesForUpdate = null;
            List<VacancyTo> tempTosExistEmployers = null;

            for (Employer e_Db : employersDb) {
                tempEmployersForUpdate = new ArrayList<>();
                tempEmployersForCreate = new ArrayList<>();
                tempVacanciesForUpdate = new ArrayList<>();
                tempTosExistEmployers = new ArrayList<>();
                if (!vacanciesDb.contains(vacancyFromTo)) {
                    if (v_To.getEmployerName().equals(e_Db.getName())) {
                        vacancyFromTo.setEmployer(e_Db);
                        tempVacanciesForUpdate.add(vacancyFromTo);
                        tempEmployersForUpdate.add(e_Db);
                        unDouble.set(false);
                    } else if (unDouble.get()) {
                        tempEmployersForCreate.add(getEmployerFromTo(v_To));
                        tempTosExistEmployers.add(v_To);
                    }
                } else {
                    Vacancy vFind = vacanciesDb.stream()
                            .filter(vDb -> vDb.getSkills().equals(vacancyFromTo.getSkills())
                                    && vDb.getTitle().equals(vacancyFromTo.getTitle())
                                    && vDb.getEmployer().getName().equals(v_To.getEmployerName())
                                    && vDb.getEmployer().getName().equals(e_Db.getName()))
                            .findAny().orElse(null);
                    if (vFind != null && unDouble.get()) {
                        v_To.setId(vFind.getId());
                        vacancyService.updateTo(v_To);
                        unDouble.set(false);
                    }
                }
            }
            employersForUpdate.addAll(tempEmployersForUpdate);
            employersForCreate.addAll(tempEmployersForCreate);
            vacanciesForUpdate.addAll(tempVacanciesForUpdate);
            tosExistEmployers.addAll(tempTosExistEmployers);
        });
        updateDb(employersForCreate, tosExistEmployers, employersForUpdate, vacanciesForUpdate, freshen);
    }

    @Transactional
    protected void updateDb(Set<Employer> employersForCreate, Set<VacancyTo> tosExistEmployers,
                            Set<Employer> employersForUpdate, Set<Vacancy> vacanciesForUpdate, Freshen freshen) {
        Freshen createdFreshen = freshenService.create(freshen);
        List<Employer> employersCreated = employerService.createList(new ArrayList<>(employersForCreate));


        System.out.println("\n\n---------------------------------------------------------------------------");
        System.out.println("tosExistEmployers("+tosExistEmployers.size()+"):");
        tosExistEmployers.forEach(e -> System.out.println("employerName : " + e.getEmployerName()));
        System.out.println("vacanciesForUpdate("+vacanciesForUpdate.size()+"):");
        vacanciesForUpdate.forEach(v -> {
            System.out.println("\n\nvacancyForUpdate {\nid : " + v.getId() + "\ntitle : " + v.getTitle() + "\nemployerName : " +
                    v.getEmployer().getName() + "\nskills : " + v.getSkills() + "\n}\n");
        });
        System.out.println("---------------------------------------------------------------------------");


        List<Vacancy> created = createVacancies(getMapVacanciesForCreate(employersCreated, tosExistEmployers), createdFreshen);
        List<Vacancy> updated = createVacancies(getMapVacanciesForUpdate(employersForUpdate, vacanciesForUpdate), createdFreshen);

        System.out.println("============================================================================");
        System.out.println("\n\ncreated("+created.size()+")");
        System.out.println("\n\nupdated("+updated.size()+"):");
        updated.forEach(v -> {
            System.out.println("\nupdated {\nid : " + v.getId() + "\ntitle : " + v.getTitle() + "\nemployerName : " +
                    v.getEmployer().getName() + "\nskills : " + v.getSkills() + "\n}\n");
        });
        System.out.println("============================================================================");


        employerService.deleteEmptyEmployers();
    }

    @Transactional
    public List<Vacancy> createVacancies(Map<Integer, List<Vacancy>> map, Freshen freshen) {
        log.info("createByMap {}", map != null ? map.size() : "there is map = null");
        List<Vacancy> newVacancies = new ArrayList<>();
        map.forEach((employerId, vacancies) -> newVacancies
                .addAll(vacancyService.createList(vacancies, employerId, freshen.getId())));
        return newVacancies;
    }

    @Transactional
    public void deleteOutdated(LocalDate reasonDateToKeep) {
        log.info("deleteVacanciesBeforeDate reasonDateToKeep {}", reasonDateToKeep);
        List<Vacancy> listToDelete = vacancyService.getAll().stream()
                .filter(vacancyTo -> reasonDateToKeep.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
        vacancyService.deleteList(listToDelete);
    }

    public static void main(String[] args) throws IOException {
        //java, php, ruby, python, javascript, kotlin
        User admin = new User(100000, "Admin", "admin@gmail.com", "admin", Role.ADMIN);
        setTestAuthorizedUser(admin);

        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new Freshen(null, LocalDateTime.now(),"java", "киев", authUserId()));
//        List<VacancyTo> vacancyTos = getAllProviders().selectBy(new Freshen(null, LocalDateTime.now(),"java", "за_рубежем", authUserId()));

        AtomicInteger i = new AtomicInteger(1);
        vacancyTos.forEach(vacancyNet -> log.info("\nvacancyNet № {}\n{}\n", i.getAndIncrement(), vacancyNet.toString()));
        log.info("\n\ncommon = {}", vacancyTos.size());
    }
}
