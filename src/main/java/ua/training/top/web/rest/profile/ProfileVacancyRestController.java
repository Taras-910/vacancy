package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.Freshen;
import ua.training.top.service.VacancyService;
import ua.training.top.to.VacancyTo;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;

@RestController
@RequestMapping(value = ProfileVacancyRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileVacancyRestController {
    static final String REST_URL = "/rest/profile/vacancies";
    public static final Logger log = LoggerFactory.getLogger(ProfileVacancyRestController.class);

    private final VacancyService vacancyService;

    public ProfileVacancyRestController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping("/{id}")
    public VacancyTo get(@PathVariable int id) {
        return vacancyService.getTo(id);
    }

    @GetMapping
    public List<VacancyTo> getAll() {
        return vacancyService.getAllTos();
    }

    @ApiIgnore
    @Transactional
    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@Valid Freshen freshen) {
        log.info("getByFilter freshen={}", freshen);
        return vacancyService.getTosByFilter(asNewFreshen(freshen));
    }
}
