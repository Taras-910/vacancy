package ua.training.top.web.vacancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "profile/vacancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyUIController extends AbstractVacancyController {
    public static final Logger log = LoggerFactory.getLogger(VacancyUIController.class);

    @Override
    @GetMapping
    public List<VacancyTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam String title,
                       @RequestParam @Nullable Integer salaryMin,
                       @RequestParam @Nullable Integer salaryMax,
                       @RequestParam @Nullable String link,
                       @RequestParam @Nullable String skills,
                       @RequestParam @Nullable Date dateTime,
                       @RequestParam @Nullable String lang,
                       @RequestParam @Nullable String workplace,
                       @RequestParam @Nullable String employerName ){
        super.create(new Vacancy(title, salaryMin, salaryMax, link, skills, dateTime, lang, workplace, LocalDateTime.now()),
                new Employer(null, employerName, workplace, link));
    }

/*    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            String errorFieldsMsg = result.getFieldErrors().stream()
                    .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                    .collect(Collectors.joining("<br>"));
            return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
        }
        if (user.isNew()) {
            super.create(user);
        } else {
            super.update(user, user.id());
        }
        return ResponseEntity.ok().build();
    }
*/



    @Override
    @GetMapping(value = "/filter")
    public List<VacancyTo> getVacanciesByLangLocFilter(
            @RequestParam String language,
            @RequestParam String workplace) {
        List<VacancyTo> vacanciesTo = super.getVacanciesByLangLocFilter(language, workplace);
        log.info("vacanciesTo {}", vacanciesTo);
        return vacanciesTo;
    }
}
/*
*
* @RestController
@RequestMapping(value = "/profile/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void create(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @RequestParam String description,
                       @RequestParam int calories) {
        super.create(new Meal(null, dateTime, description, calories));
    }

    @Override
    @GetMapping(value = "/filter")
    public List<MealTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
*/
