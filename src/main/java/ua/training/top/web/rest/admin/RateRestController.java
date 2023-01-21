package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.Rate;
import ua.training.top.service.RateService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = RateRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RateRestController {
    static final String REST_URL = "/rest/admin/rates";
    public static final Logger log = LoggerFactory.getLogger(RateRestController.class);
    private final RateService service;

    public RateRestController(RateService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Rate get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping
    public List<Rate> getAll() {
        return service.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rate> create(@RequestBody @Valid Rate rate) {
        Rate created = service.create(rate);
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
}
