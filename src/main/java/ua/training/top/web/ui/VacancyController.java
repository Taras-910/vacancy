package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.service.VacancyService;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.ValidationUtil.assureIdConsistent;

@Controller
public class VacancyController {
    private static final Logger log = LoggerFactory.getLogger(VacancyController.class);

    private final VacancyService service;
    private final VoteController voteController;


    public VacancyController(VacancyService service, VoteController controller) {
        this.service = service;
        this.voteController = controller;
    }

    public Vacancy get(int id, int employerId) {
        log.info("get vacancy for id {}", id);
        return service.get(id, employerId);
    }

    public void delete(int vacancyId, int employerId) {
        log.info("delete vacancy {} of employer {}", vacancyId, employerId);
        service.delete(vacancyId, employerId);
    }

    public void deleteListOfVacancies(int employerId) {
        log.info("deleteAll for employerId {}", employerId);
        service.deleteEmployerVacancies(employerId);
    }
    @Transactional
    public Vacancy create(Vacancy vacancy, int employerId) {
        log.info("create vacancy {} for employerId {}", vacancy, employerId);
        return service.create(vacancy, employerId);
    }


    @Transactional
    public List<Vacancy> createAll(List<Vacancy> vacancies, int employerId) {
        if (vacancies != null) log.info("createAll {} vacancies for employerId {}", vacancies.size() , employerId);
        return service.createAll(vacancies, employerId);
    }

    public void update(@Valid Vacancy vacancy, int vacancyId, int employerId) {
        log.info("update vacancy {} for vacancyId {} and employerId {}", vacancy, vacancyId, employerId);
        assureIdConsistent(vacancy, vacancyId);
        service.update(vacancy, employerId);
    }

    public List<VacancyTo> getAllTos() {
        log.info("getAllTos");
        List<Vacancy> vacancies = service.getAll();
        List<Vote> votes = voteController.getAllForAuthUser();
        return VacancyUtil.getTos(vacancies, votes);
    }

    public List<Vacancy> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public void deleteAll() {
        log.info("deleteAll");
        service.deleteAll();
    }

    public void enable(@PathVariable int vacancyId, @RequestParam boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", vacancyId);
        voteController.create(vacancyId);
    }

}
