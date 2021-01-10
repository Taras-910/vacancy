package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.VacancyUtil.getResult;

@RestController
@RequestMapping(value = VacancyRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyRestController {
    static final String REST_URL = "/rest/profile/vacancies";
    public static final Logger log = LoggerFactory.getLogger(VacancyRestController.class);
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
        return vacancyService.getAllTos();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        vacancyService.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid VacancyTo vacancyTo) {
        vacancyService.update(vacancyTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacancy> createVacancyEmployer(@RequestBody @Valid VacancyTo vacancyTo) {
        Vacancy created = vacancyService.createVacancyAndEmployer(vacancyTo);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@RequestParam String language, @RequestParam String workplace) {
        return vacancyService.getTosByFilter(language, workplace);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        voteService.setVote(vacancyId, enabled);
    }

    @GetMapping(value = "/refresh")
    public Freshen getNewFreshen() {
        log.info("getNewFreshen by user {}", authUserId());
        return new Freshen(null, LocalDateTime.now(), "", "", authUserId());
    }

    @PostMapping(value = "/refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> refreshDB(@Nullable Freshen doubleString, BindingResult result) {
        if (result.hasErrors()) {
            getResult(result);
        }
        aggregatorController.refreshDB(doubleString);
        return ResponseEntity.ok().build();
    }
}
