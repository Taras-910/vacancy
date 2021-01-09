package ua.training.top.web.rest.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = FreshenRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class FreshenRestController {
    static final String REST_URL = "/rest/admin/freshen";

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
}
