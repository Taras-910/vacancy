package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;

import javax.validation.Valid;
import java.util.List;

@ApiIgnore
@RestController
@RequestMapping(value = "profile/vacancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyUIController {
    public static final Logger log = LoggerFactory.getLogger(VacancyUIController.class);
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;

    @GetMapping("/{id}")
    public VacancyTo get(@PathVariable int id) {
        return vacancyService.getTo(id);
    }

    @GetMapping
    public List<VacancyTo> getAll() {
        List <VacancyTo> vacancyTos = vacancyService.getAllTos();
        log.info("vacancyTos {}", vacancyTos);
        return vacancyTos;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        vacancyService.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@Valid VacancyTo vacancyTo) {
        log.info("createOrUpdate vacancyTo={}", vacancyTo);
        if (vacancyTo.isNew()) {
            vacancyService.createVacancyAndEmployer(vacancyTo);
        } else {
            vacancyService.updateTo(vacancyTo);
        }
    }

    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@RequestParam String language, @RequestParam String workplace) {
        log.info("getByFilter language={} workplace={}", language, workplace);
        return vacancyService.getTosByFilter(language, workplace);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        voteService.setVote(vacancyId, enabled);
    }
}
