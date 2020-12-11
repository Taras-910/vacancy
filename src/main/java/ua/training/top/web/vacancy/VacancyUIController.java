package ua.training.top.web.vacancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ua.training.top.to.VacancyTo;

import java.util.List;

import static ua.training.top.util.DateTimeUtil.thisDay;

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
    public void create(@RequestParam @Nullable String title,
                       @RequestParam @Nullable String employerName,
                       @RequestParam @Nullable String address,
                       @RequestParam @Nullable Integer salaryMin,
                       @RequestParam @Nullable Integer salaryMax,
                       @RequestParam @Nullable String url,
                       @RequestParam @Nullable String skills,
                       @RequestParam @Nullable String languageCode ){
                super.create(new VacancyTo(null, title, employerName, address, salaryMin, salaryMax, url, skills, thisDay, languageCode, false));
    }

 /*   @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid Vacancy vacancy, BindingResult result) {

        if (result.hasErrors()) {
            String errorFieldsMsg = result.getFieldErrors().stream()
                    .map(fe -> String.format("[%s] %s", fe.getField(), fe.getDefaultMessage()))
                    .collect(Collectors.joining("<br>"));
            return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
        }
        if (vacancy.isNew()) {
            super.create(vacancy);
        } else {
            super.update(vacancy, vacancy.id());
        }
        return ResponseEntity.ok().build();
    }
*/


    @Override
    @GetMapping(value = "/filter")
    public List<VacancyTo> getVacanciesByLangLocFilter(
            @RequestParam String language,
            @RequestParam String workplace) {
        log.info("getVacanciesByLangLocFilter language {} workplace {}", language, workplace);
        List<VacancyTo> vacanciesTo = super.getVacanciesByLangLocFilter(language, workplace);
        return vacanciesTo;
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        super.enable(vacancyId, enabled);
    }

}
