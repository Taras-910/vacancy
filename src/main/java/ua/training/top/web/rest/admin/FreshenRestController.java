package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getResult;

@RestController
@RequestMapping(value = FreshenRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class FreshenRestController {
    static final String REST_URL = "/rest/admin/freshen";
    public static final Logger log = LoggerFactory.getLogger(FreshenRestController.class);

    @Autowired
    private FreshenService service;

    @GetMapping
    public List<Freshen> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Freshen get(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Freshen> createWithLocation(@RequestBody Freshen freshen) {
        Freshen created = service.create(freshen);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Freshen freshen, @PathVariable int id) {
        service.update(freshen, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Valid Freshen freshen, BindingResult result) {
        log.info("refreshDB freshen {}", freshen);
        if (result.hasErrors()) {
            getResult(result);
        }
        service.refreshDB(freshen);
    }

}
