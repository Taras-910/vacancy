package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.model.Vacancy;
import ua.training.top.repository.VacancyRepository;
import ua.training.top.util.ValidationUtil;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.ui.VacancyController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.ValidationUtil.*;

@Service
public class VacancyService {
    private static final Logger log = LoggerFactory.getLogger(VacancyController.class);

    private final VacancyRepository repository;

    public VacancyService(VacancyRepository repository) {
        this.repository = repository;
    }

    public Vacancy get(int id, int employerId) {
        log.info("get vacancy for id {}", id);
        return checkNotFoundWithId(repository.get(id, employerId), id);
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    public void delete(int vacancyId, int employerId) {
        log.info("delete vacancy {} of employer {}", vacancyId, employerId);
        checkNotFoundWithId(repository.delete(vacancyId, employerId), vacancyId);
    }

    public void deleteEmployerVacancies(int employerId) {
        log.info("deleteAll for employerId {}", employerId);
        checkNotFoundWithId(repository.deleteEmployerVacancies(employerId), employerId);
    }
    @Transactional
    public Vacancy create(@Valid @NotEmpty Vacancy vacancy, int employerId) {
        log.info("create vacancy {} for employerId {}", vacancy, employerId);
        Assert.notNull(vacancy, "vacancy must not be null");
        checkNew(vacancy);
        return checkNotFound(repository.save(vacancy, employerId), "employerId=" + employerId);
    }

    @Transactional
    public List<Vacancy> createAll(@Valid @NotEmpty List<@NotEmpty Vacancy> vacancies, int employerId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId {}", vacancies.size(), employerId);
        List<Vacancy> created;
        try {
            vacancies.forEach(ValidationUtil::checkNew);
            vacancies.forEach(v -> Assert.notNull(v, "vacancy must not be null"));
            vacancies.stream().map(vacancy -> vacancy.getName().toLowerCase()).distinct().collect(Collectors.toList());
            created = checkNotFound(repository.saveAll(vacancies, employerId), "employerId=" + employerId);
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException | ExceptionInInitializerError e) {
            throw new NotFoundException("error argument List (" + vacancies + ")");
        }
        return created;
    }


    public void update(@Valid Vacancy vacancy, int employerId) {
        log.info("update vacancy {} for employerId {}", vacancy, employerId);
        try {
            checkNotFoundWithId(repository.save(vacancy, employerId), vacancy.id());
        } catch (IllegalArgumentException | DataIntegrityViolationException | NullPointerException e) {
            throw new NotFoundException("error data of " + vacancy);
        }
    }

    public void deleteAll() {
        log.info("deleteAll");
        repository.deleteAll();
    }

}
