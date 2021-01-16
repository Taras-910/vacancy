package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;

import java.util.List;

@RestController
@RequestMapping(value = ProfileVacancyRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVacancyRestController {
    static final String REST_URL = "/rest/profile/vacancies";
    public static final Logger log = LoggerFactory.getLogger(ProfileVacancyRestController.class);
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

    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@RequestParam String language, @RequestParam String workplace) {
        return vacancyService.getTosByFilter(language, workplace);
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        voteService.setVote(vacancyId, enabled);
    }
}
