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
import ua.training.top.util.EmployerUtil;

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
import static ua.training.top.util.EmployerUtil.getEmployersForCreate;
import static ua.training.top.util.VacancyUtil.*;

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
        List<Vacancy> vacanciesDb = vacancyService.getByFilter(freshen);
//        deleteOutdated(reasonDateToKeep);
        List<Employer> employersDb = employerService.getAll();
        List<Employer> employersForCreate = getEmployersForCreate(vacancyTos, employersDb);
        List<Employer> employersAll = new ArrayList<>(employersDb);
        if(!employersForCreate.isEmpty()) {
            employersAll.addAll(employerService.createList(employersForCreate));
        }
        List<Employer> employersSuitable = EmployerUtil.getEmployersSuitable(vacancyTos, employersAll);
        List<Vacancy> vacancyForCreate = new ArrayList<>();
        Set<Vacancy> vacanciesForUpdate = new HashSet<>();
        Set<Vacancy> vacanciesForCreate = new HashSet<>();
        vacancyTos.forEach(vTo -> {
            AtomicBoolean unDouble = new AtomicBoolean(true);
            Vacancy vacancyFromTo = fromTo(vTo);
            for (Employer e_Suitable : employersSuitable) {
                if (isNotContains(vTo, vacanciesDb)) {
                    if (vTo.getEmployerName().equals(e_Suitable.getName())) {
                        vacancyFromTo.setEmployer(e_Suitable);
                        vacancyForCreate.add(vacancyFromTo);
                        unDouble.set(false);
                    }
                } else {
                    if(vacanciesDb.size() != 0) {
                        Vacancy vFind = vacanciesDb.stream()
                                .filter(vDb -> vDb.getSkills().equals(vacancyFromTo.getSkills())
                                        && vDb.getTitle().equals(vacancyFromTo.getTitle())
                                        && vDb.getEmployer().getName().equals(vTo.getEmployerName())
                                        && vDb.getEmployer().getName().equals(e_Suitable.getName()))
                                .findAny().orElse(null);
                        if (vFind != null && isNotSame(vTo, vFind) && unDouble.get()) {
                            vTo.setId(vFind.getId());
                            Vacancy vacancy_from = fromTo(vTo);
                            vacancy_from.setEmployer(e_Suitable);
                            vacanciesForUpdate.add(vacancy_from);
                            unDouble.set(false);
                        }
                    }
                }
            }
            vacanciesForCreate.addAll(vacancyForCreate);
        });
        updateDb(vacanciesForCreate, vacanciesForUpdate, freshen);
    }

    @Transactional
    protected void updateDb(Set<Vacancy> vacanciesForCreate, Set<Vacancy> vacanciesForUpdate, Freshen freshen) {
        Freshen createdFreshen = freshenService.create(freshen);
        if(!vacanciesForCreate.isEmpty()) {
            List<Vacancy> created = createVacancies(getMapVacanciesForCreate(vacanciesForCreate), createdFreshen);
        }
        if(!vacanciesForUpdate.isEmpty()) {
            List<Vacancy> updated = updateVacancies(vacanciesForUpdate, createdFreshen);
        }
        employerService.deleteEmptyEmployers();
    }

    @Transactional
    public List<Vacancy> updateVacancies(Set<Vacancy> vacancies, Freshen freshen) {
        log.info("updateVacancies {}", vacancies != null ? vacancies.size() : "there is vacancies = null");
        List<Vacancy> updatedVacancies = new ArrayList<>();
        vacancies.forEach(v -> updatedVacancies.add(vacancyService.update(v, freshen.getId())));
        return updatedVacancies;
    }

    @Transactional
    public List<Vacancy> createVacancies(Map<Integer, List<Vacancy>> map, Freshen freshen) {
        log.info("createByMap {}", map != null ? map.size() : "there is map = null");
        List<Vacancy> newVacancies = new ArrayList<>();
        map.forEach((employerId, vacancies) -> newVacancies.addAll(vacancyService.createList(vacancies, employerId, freshen.getId())));
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
