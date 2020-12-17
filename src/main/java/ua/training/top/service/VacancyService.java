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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.getVacancyFromTo;
import static ua.training.top.util.ValidationUtil.checkNotFound;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;
import static ua.training.top.util.refresh.EmployerUtil.getEmployerFromTo;

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
    public void deleteEmployerVacancies(int employerId) {
        log.info("deleteAll for employerId {}", employerId);
        checkNotFoundWithId(vacancyRepository.deleteEmployerVacancies(employerId), employerId);
    }

    @Transactional
    public void deleteBeforeDate(LocalDateTime recordedDate) {
        log.info("deleteByDateRecorded recordedDate {}", recordedDate);
        vacancyRepository.deleteBeforeDate(recordedDate);
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
        Employer newEmployer = checkNotFound(employerRepository.save(getEmployerFromTo(vacancyTo)), "this data");
        return checkNotFound(vacancyRepository.save(getVacancyFromTo(vacancyTo), newEmployer.getId()),
                "employerId=" + newEmployer.getId());
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

    public List<Vacancy> getAllByLanguage(String language) {
        log.info("getAllByLanguage {}", language);
        return vacancyRepository.getAllByLanguage(language);
    }

    public List<Vacancy> getAllByWorkplace(String workplace) {
        log.info("getAllByWorkplace {}", workplace);
        return vacancyRepository.getAllByWorkplace(workplace);
    }

    public List<Vacancy> getByFilter(String language, String workplace) {
        log.info("getByFilter language={} workplace={}",language ,workplace);
        Set<Vacancy> vacancies = new LinkedHashSet<>();
        if (!language.isEmpty() && !workplace.isEmpty()) {
            vacancies.addAll(vacancyRepository.getAllByFilter(language, workplace));
            vacancies.stream().filter(v ->
                    (v.getTitle().toLowerCase().contains(language) || v.getSkills().toLowerCase().contains(language))
                    && (v.getEmployer().getAddress().toLowerCase().contains(workplace) || v.getWorkplace().equals(workplace)))
                    .collect(Collectors.toList());
        }
        if (workplace.isEmpty()) {
            if(language.isEmpty())
            vacancies.addAll(getAll());
            else  {
                vacancies.addAll(getAllByWorkplace(workplace));
                vacancies.stream().filter(v -> v.getEmployer().getAddress().toLowerCase().contains(workplace) || v.getWorkplace().equals(workplace))
                        .collect(Collectors.toList());
            }
        }
        else {
            vacancies.addAll(getAllByLanguage(language));
            vacancies.stream().filter(v -> (v.getTitle().toLowerCase().contains(language)
                    || v.getSkills().toLowerCase().contains(language))).collect(Collectors.toList());
        }
        return new ArrayList<>(vacancies);
    }

    @Transactional
    public List<Vacancy> createByMap(Map<Integer, List<Vacancy>> map) {
        log.info("createByMap {}", map != null ? map.size() : "there is map = null");
        List<Vacancy> newVacancies = new ArrayList<>();
        map.forEach((employerId, vacancies) -> newVacancies.addAll(createAll(vacancies, employerId)));
        return newVacancies;
    }
}
