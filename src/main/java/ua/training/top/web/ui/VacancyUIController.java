package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.Freshen;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;

@ApiIgnore
@RestController
@RequestMapping(value = "profile/vacancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyUIController {
    public static final Logger log = LoggerFactory.getLogger(VacancyUIController.class);
    private final VacancyService vacancyService;
    private final VoteService voteService;

    public VacancyUIController(VacancyService vacancyService, VoteService voteService) {
        this.vacancyService = vacancyService;
        this.voteService = voteService;
    }

    @GetMapping("/{id}")
    public VacancyTo get(@PathVariable int id) {
        return vacancyService.getTo(id);
    }

    @GetMapping
    public List<VacancyTo> getAll() {
        return vacancyService.getAllTos();
    }

    @Transactional
    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@Valid @ModelAttribute Freshen freshen) {
        log.info("getByFilter language={} level={} workplace={}", freshen.getLanguage(), freshen.getLevel(), freshen.getWorkplace());
        return vacancyService.getTosByFilter(asNewFreshen(freshen));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@Valid @ModelAttribute VacancyTo vacancyTo) {
        log.info("createOrUpdate vacancyTo={}", vacancyTo);
        if (vacancyTo.isNew()) {
            vacancyService.createVacancyAndEmployer(vacancyTo, getFreshenFromTo(vacancyTo));
        } else {
            vacancyService.updateTo(vacancyTo);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        vacancyService.delete(id);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        voteService.setVote(vacancyId, enabled);
    }
}
