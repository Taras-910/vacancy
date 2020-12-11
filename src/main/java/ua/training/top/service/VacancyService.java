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
import ua.training.top.web.jsp.VacancyController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.ValidationUtil.*;

@Service
public class VacancyService {
    private static final Logger log = LoggerFactory.getLogger(VacancyController.class);

    private final VacancyRepository vacancyRepository;
    private final EmployerRepository employerRepository;

    public VacancyService(VacancyRepository repository, EmployerRepository employerRepository) {
        this.vacancyRepository = repository;
        this.employerRepository = employerRepository;
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
    public Vacancy createByEmployerId(@Valid @NotEmpty Vacancy vacancy, int employerId) {
        log.info("create vacancy {} for employer {}", vacancy, employerId);
        Assert.notNull(vacancy, "vacancy must not be null");
        checkNew(vacancy);
        return checkNotFound(vacancyRepository.save(vacancy, employerId), "employerId=" + employerId);
    }

    @Transactional
    public Vacancy create(@Valid @NotEmpty VacancyTo vacancyTo) {
        log.info("create vacancyTo {}", vacancyTo);
        Employer newEmployer = checkNotFound(employerRepository.save(getEmployerFromTo(vacancyTo)), "this data");
        return checkNotFound(vacancyRepository.save(getVacancyFromTo(vacancyTo), newEmployer.getId()), "employerId=" + newEmployer.getId());
    }


    @Transactional
    public List<Vacancy> createAll(@Valid @NotEmpty List<@NotEmpty Vacancy> vacancies, int employerId) {
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


    public void update(@Valid Vacancy vacancy, int employerId) {
        log.info("update vacancy {} for employerId {}", vacancy, employerId);
        try {
            checkNotFoundWithId(vacancyRepository.save(vacancy, employerId), vacancy.id());
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error data of " + vacancy);
        }
    }

    public void deleteAll() {
        log.info("deleteAll");
        vacancyRepository.deleteAll();
    }

    public List<Vacancy> getVacanciesByLangLocFilter(String language, String workplace) {
        log.info("getVacancyLangLocFilter");
        language = language != null ? language.toLowerCase() : LANGUAGE_DEFAULT;
        workplace = workplace != null ? workplace.toLowerCase() : WORKPLACE_DEFAULT;
        return vacancyRepository.getVacanciesByLangLocFilter(language, workplace);
    }
}
