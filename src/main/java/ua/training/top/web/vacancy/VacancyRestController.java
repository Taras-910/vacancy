package ua.training.top.web.vacancy;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = VacancyRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VacancyRestController extends AbstractVacancyController {
    static final String REST_URL = "/rest/profile/vacancies";

    @Override
    @GetMapping("/{id}")
    public Vacancy get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @Override
    @GetMapping
    public List<VacancyTo> getAll() {
        return super.getAll();
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Vacancy vacancy, @PathVariable int id) {
        super.update(vacancy, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vacancy> createWithLocation(@RequestBody Vacancy vacancy, @RequestParam String employerName) {
        Vacancy created = super.create(vacancy, new Employer(null, employerName, vacancy.getWorkplace(), vacancy.getLink()));

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

/*
    @Override
    @GetMapping("/filter")
    public List<VacancyTo> getBetween(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
*/
}

