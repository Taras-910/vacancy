package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.Employer;
import ua.training.top.service.EmployerService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ua.training.top.util.MessageUtil.error_data;

@RestController
@RequestMapping(value = EmployerRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployerRestController {
    static final String REST_URL = "/rest/admin/employers";
    public static final Logger log = LoggerFactory.getLogger(EmployerRestController.class);
    private final EmployerService service;

    public EmployerRestController(EmployerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Employer get(@PathVariable int id) {
        log.info("get");
        return service.get(id);
    }

    @GetMapping
    public List<Employer> getAll() {
        return service.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employer> create(@RequestBody @Valid Employer employer) {
        Employer created = service.create(employer);
        ResponseEntity<Employer> entity;
        try {
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}")
                    .buildAndExpand(created.getId()).toUri();
            entity = ResponseEntity.created(uriOfNewResource).body(created);
        } catch (Exception e) {
            throw new DataIntegrityViolationException(error_data);
        }
        return entity;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Employer employer) {
        log.info("update employer={}", employer);
        service.update(employer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}
