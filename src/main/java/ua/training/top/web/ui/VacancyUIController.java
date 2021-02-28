package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import static ua.training.top.util.VacancyCheckUtil.URL_ERROR_MESSAGE;
import static ua.training.top.util.VacancyCheckUtil.URL_MATCHER;

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
        return vacancyService.getAllTos();
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
        if(!vacancyTo.getUrl().matches(URL_MATCHER)) {
            throw new IllegalArgumentException(URL_ERROR_MESSAGE);
        }
        if (vacancyTo.isNew()) {
            vacancyService.createVacancyAndEmployer(vacancyTo, getFreshenFromTo(vacancyTo));
        } else {
            vacancyService.updateTo(vacancyTo);
        }
    }

    @Transactional
    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@Valid Freshen freshen) {
        log.info("getByFilter language={} workplace={}", freshen.getLanguage(), freshen.getWorkplace());
        return vacancyService.getTosByFilter(asNewFreshen(freshen));
    }

    @GetMapping(value = "/count")
    public int getCountToday() {
        log.info("getCountToday");
        int count = vacancyService.getCountToday();
        log.info("count {}", count);
        return count;
    }

    @GetMapping(value = "/last")
    public int getCountLastUpgrade() {
        log.info("getCountLast");
        int data =  vacancyService.getCountLast();
        log.info("data {}", data);
        return data;
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        voteService.setVote(vacancyId, enabled);
    }
}
