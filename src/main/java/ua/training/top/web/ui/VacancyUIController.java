package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.model.Freshen;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getResult;
import static ua.training.top.util.VacancyUtil.getValidFreshen;

@RestController
@RequestMapping(value = "profile/vacancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyUIController {
    public static final Logger log = LoggerFactory.getLogger(VacancyUIController.class);
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private AggregatorController aggregatorController;

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
    public void createOrUpdate(@Valid VacancyTo vacancyTo, BindingResult result) {
        if (result.hasErrors()) {
            getResult(result);
        }
        if (vacancyTo.isNew()) {
            vacancyService.createVacancyAndEmployer(vacancyTo);
        } else {
            vacancyService.update(vacancyTo);
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

    @PostMapping(value = "/refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Nullable Freshen doubleString, BindingResult result) {
        log.info("refreshDB task {}", doubleString);
        if (result.hasErrors()) {
            getResult(result);
        }
        aggregatorController.refreshDB(getValidFreshen(doubleString));
    }
}
