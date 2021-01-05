package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;
import ua.training.top.web.AbstractVacancyController;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getResult;

@RestController
@RequestMapping(value = "profile/vacancies", produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyUIController extends AbstractVacancyController {
    public static final Logger log = LoggerFactory.getLogger(VacancyUIController.class);

    @Override
    @GetMapping("/{id}")
    public VacancyTo get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<VacancyTo> getAll() {
        List <VacancyTo> vacancyTos = super.getAll();
        log.info("vacancyTos {}", vacancyTos);
        return vacancyTos;
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid VacancyTo vacancyTo, BindingResult result) {
        if (result.hasErrors()) {
            getResult(result);
        }
        if (vacancyTo.isNew()) {
            super.createVacancyAndEmployer(vacancyTo);
        } else {
            super.update(vacancyTo);
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(value = "/filter")
    public List<VacancyTo> getByFilter(@RequestParam String language, @RequestParam String workplace) {
        log.info("getByFilter language={} workplace={}", language, workplace);
        List<VacancyTo> vacancyTos =  super.getByFilter(language, workplace);
        return vacancyTos;
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean enabled) {
        super.setVote(vacancyId, enabled);
    }

    @PostMapping(value = "/refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> refreshDB(@Valid DoubleString task, BindingResult result) {
        log.info("refreshDB task {}", task);
        if (result.hasErrors()) {
            getResult(result);
        }
        super.refreshDB(task);
        return ResponseEntity.ok().build();
    }
}
