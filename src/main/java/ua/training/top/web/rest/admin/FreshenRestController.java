package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;

@RestController
@RequestMapping(value = FreshenRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class FreshenRestController {
    static final String REST_URL = "/rest/admin/freshen";
    public static final Logger log = LoggerFactory.getLogger(FreshenRestController.class);

    @Autowired
    private FreshenService service;

    @GetMapping("/{id}")
    public Freshen get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping
    public List<Freshen> getAll() {
        return service.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Freshen> create(@RequestBody @Valid Freshen freshen) {
        Freshen created = service.create(freshen);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid Freshen freshen, @PathVariable int id) {
        service.update(freshen, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @ApiIgnore
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Valid Freshen freshen) {
        log.info("refreshDB freshen {}", freshen);
        service.refreshDB(asNewFreshen(freshen));
    }

}
