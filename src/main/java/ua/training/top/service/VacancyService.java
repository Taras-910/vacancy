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
import ua.training.top.util.ValidationUtil;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.getEmployerFromTo;
import static ua.training.top.util.VacancyUtil.getVacancyFromTo;
import static ua.training.top.util.ValidationUtil.*;

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

    public void delete(int id) {
        log.info("delete vacancy {}", id);
        checkNotFoundWithId(vacancyRepository.delete(id), id);
    }

    public void deleteEmployerVacancies(int employerId) {
        log.info("deleteAll for employerId {}", employerId);
        checkNotFoundWithId(vacancyRepository.deleteEmployerVacancies(employerId), employerId);
    }

    @Transactional
    public Vacancy createByToAndEmployerId(Vacancy vacancy, int employerId) {
        log.info("create vacancy {} for employer {}", vacancy, employerId);
        Assert.notNull(vacancy, "vacancy must not be null");
        checkNew(vacancy);
        return checkNotFound(vacancyRepository.save(vacancy, employerId), "employerId=" + employerId);
    }

    @Transactional
    public Vacancy createVacancyAndEmployer(VacancyTo vacancyTo) {
        log.info("create vacancyTo {}", vacancyTo);

        Employer newEmployer = checkNotFound(employerRepository.save(getEmployerFromTo(vacancyTo)), "this data");
        return checkNotFound(vacancyRepository.save(getVacancyFromTo(vacancyTo), newEmployer.getId()), "employerId=" + newEmployer.getId());
    }


    @Transactional
    public List<Vacancy> createAll(List<@NotEmpty Vacancy> vacancies, int employerId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId {}", vacancies.size(), employerId);
        List<Vacancy> created;
        try {
            vacancies.forEach(ValidationUtil::checkNew);
            vacancies.forEach(v -> Assert.notNull(v, "vacancy must not be null"));
            vacancies.stream().map(vacancy -> vacancy.getTitle().toLowerCase()).distinct().collect(Collectors.toList());
            created = checkNotFound(vacancyRepository.saveAll(vacancies, employerId), "employerId=" + employerId);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException | ExceptionInInitializerError e) {
            throw new NotFoundException("error argument List (" + vacancies + ")");
        }
        return created;
    }


    public void update(VacancyTo vacancyTo) {
        log.info("update vacancyTo {}", vacancyTo);
        try {
            checkNotFoundWithId(vacancyRepository.save(getVacancyFromTo(vacancyTo), get(vacancyTo.id()).getEmployer().id()), vacancyTo.id());
            voteService.deleteAllByVacancyId(vacancyTo.id());
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error data of " + vacancyTo);
        }
    }

    public void deleteAll() {
        log.info("deleteAll");
        vacancyRepository.deleteAll();
    }

    public List<Vacancy> getAllByLanguage(String workplace) {
        log.info("deleteAll");
        return vacancyRepository.getAllByLanguage(workplace);
    }

    public List<Vacancy> getAllByWorkplace(String language) {
        log.info("deleteAll");
        return vacancyRepository.getAllByWorkplace(language);
    }

    public List<Vacancy> getByFilter(String language, String workplace) {
        log.info("getVacancyLangLocFilter language {} workplace{}",language ,workplace);
        Set<Vacancy> vacancies = new LinkedHashSet<>();
        if (language != null && !language.isEmpty() && workplace != null && !workplace.isEmpty()) {
            vacancies.addAll(vacancyRepository.getAllByFilter(language, workplace));
        }
        if ((language == null || language.equals("")) && (workplace == null || workplace.equals(""))) {
            vacancies.addAll(getAll());
        } else {
            vacancies.addAll(language != null && !language.isEmpty() ? getAllByWorkplace(language) : getAllByLanguage(workplace));
        }
        return new ArrayList<>(vacancies);
    }
}
