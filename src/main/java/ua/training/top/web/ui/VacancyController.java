package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Vacancy;
import ua.training.top.service.VacancyService;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.ValidationUtil.assureIdConsistent;

@Controller
public class VacancyController {
    private static final Logger log = LoggerFactory.getLogger(VacancyController.class);

    private final VacancyService service;

    public VacancyController(VacancyService service) {
        log.info("constructor VacancyController service {}", service==null);
        this.service = service;
    }

    public Vacancy get(int id, int employerId) {
        log.info("get vacancy for id {}", id);
        return service.get(id, employerId);
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public void delete(int vacancyId, int employerId) {
        log.info("delete vacancy {} of employer {}", vacancyId, employerId);
        service.delete(vacancyId, employerId);
    }

    public void deleteListOfVacancies(int employerId) {
        log.info("deleteAll for employerId {}", employerId);
        service.deleteListOfVacancies(employerId);
    }
    @Transactional
    public Vacancy create(Vacancy vacancy, int employerId) {
        log.info("create vacancy {} for employerId {}", vacancy, employerId);
        return service.create(vacancy, employerId);
    }

    @Transactional
    public List<Vacancy> createListOfVacancies(List<Vacancy> vacancies, int employerId) {
        log.info("create List vacancies {} for employerId {}", vacancies, employerId);
        return service.createListOfVacancies(vacancies, employerId);
    }

    public void update(@Valid Vacancy vacancy, int vacancyId, int employerId) {
        log.info("update vacancy {} for vacancyId {} and employerId {}", vacancy, vacancyId, employerId);
        assureIdConsistent(vacancy, vacancyId);
        service.update(vacancy, employerId);
    }
}
