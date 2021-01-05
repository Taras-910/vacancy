package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.EmployerRepository;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.getSiteName;
import static ua.training.top.util.VacancyUtil.getVacancyFromTo;
import static ua.training.top.util.ValidationUtil.checkNotFound;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VacancyService {
    private static final Logger log = LoggerFactory.getLogger(VacancyService.class);

    private final VacancyRepository vacancyRepository;
    private final EmployerRepository employerRepository;
    private final VoteService voteService;

    public VacancyService(VacancyRepository repository, EmployerRepository employerRepository, VoteService voteService) {
        this.vacancyRepository = repository;
        this.employerRepository = employerRepository;
        this.voteService = voteService;
    }

    public Vacancy get(int id) {
        log.info("get vacancy for id {}", id);
        return checkNotFoundWithId(vacancyRepository.get(id), id);
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return vacancyRepository.getAll();
    }

    @Transactional
    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(vacancyRepository.delete(id), id);
    }

    @Transactional
    public void deleteVacanciesOfEmployer(int employerId) {
        log.info("deleteAll for employerId {}", employerId);
        checkNotFoundWithId(vacancyRepository.deleteVacanciesOfEmployer(employerId), employerId);
    }
    @Transactional
    public void deleteList(List<Vacancy> list) {
        log.info("deleteList {}", list);
        vacancyRepository.deleteList(list);
    }

    @Transactional
    public void deleteBeforeDate(LocalDate localDate) {
        log.info("deleteByDateRecorded reasonToKeepDate {}", localDate);
        List<Vacancy> listToDelete = vacancyRepository.getAll().stream()
                .filter(vacancyTo -> localDate.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
        deleteList(listToDelete);
    }

    @Transactional
    public Vacancy createUpdate(Vacancy vacancy, int employerId) {
        log.info("create vacancy {} for employer {}", vacancy, employerId);
        Assert.notNull(vacancy, "vacancy must not be null");
        return checkNotFound(vacancyRepository.save(vacancy, employerId), "employerId=" + employerId);
    }

    @Transactional
    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo) {
        log.info("create vacancyTo {}", vacancyTo);
        Employer employer = employerRepository.getOrCreate(new Employer(null, vacancyTo.getEmployerName(), vacancyTo.getAddress(), getSiteName(vacancyTo.getUrl())));
        return checkNotFound(vacancyRepository.save(getVacancyFromTo(vacancyTo), employer.getId()),
                "employerId=" + employer.getId());
    }

    @Transactional
    public List<Vacancy> createAll(@Valid @NotEmpty List<@NotEmpty Vacancy> vacancies, int employerId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId {}", vacancies.size(), employerId);
        List<Vacancy> created;
        try {
//            vacancies.forEach(ValidationUtil::checkNew);
            vacancies.forEach(v -> Assert.notNull(v, "vacancy must not be null"));
            vacancies.stream().map(vacancy -> vacancy.getTitle().toLowerCase()).distinct().collect(Collectors.toList());
            created = checkNotFound(vacancyRepository.saveAll(vacancies, employerId), "employerId=" + employerId);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException | ExceptionInInitializerError e) {
            throw new NotFoundException(this.getClass().getSimpleName().concat(" method createAll:")
                    .concat(" error argument List = ").concat(vacancies.toString()));
        }
        return created;
    }

    @Transactional
    public void update(VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        try {
            Vacancy vacancy = getVacancyFromTo(vacancyTo);
            checkNotFoundWithId(vacancyRepository.save(vacancy, get(vacancyTo.id()).getEmployer().id()), vacancyTo.id());
            voteService.deleteAllByVacancyId(vacancyTo.id());
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException(this.getClass().getSimpleName().concat(" method update:").concat(" error data of ").concat(vacancyTo.toString()));
        }
    }

    public List<Vacancy> getByFilter(String language, String workplace) {
        log.info("getByFilter language={} workplace={}", language, workplace);
        if (!language.isEmpty()) {
            if (!workplace.isEmpty()) {
                log.info("getByFilter language={} workplace={}", language, workplace);
                return vacancyRepository.getAllByFilter(language, workplace);
            } else {
                log.info("getAllByLanguage language={}", language);
                return vacancyRepository.getAllByLanguage(language);
            }
        } else {
            if (!workplace.isEmpty()) {
                log.info("getByFilter workplace={}", workplace);
                return vacancyRepository.getAllByWorkplace(workplace);
            } else return getAll();
        }
    }

    @Transactional
    public List<Vacancy> createByMap(Map<Integer, List<Vacancy>> map) {
        log.info("createByMap {}", map != null ? map.size() : "there is map = null");
        List<Vacancy> newVacancies = new ArrayList<>();
        map.forEach((employerId, vacancies) -> newVacancies.addAll(createAll(vacancies, employerId)));
        return newVacancies;
    }
}
