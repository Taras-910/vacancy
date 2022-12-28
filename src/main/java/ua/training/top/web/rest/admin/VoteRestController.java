package ua.training.top.web.rest.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.Vote;
import ua.training.top.service.VoteService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {
    static final String REST_URL = "/rest/admin/votes";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final VoteService service;

    public VoteRestController(VoteService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Vote get(@PathVariable int id) {
        log.info("get {}", id);
        return service.get(id);
    }

    @GetMapping
    public List<Vote> getAll() {
        log.info("getAll votes");
        return service.getAll();
    }

    @GetMapping("/auth")
    public List<Vote> getAuth() {
        log.info("get all for authUserId");
        return service.getAllForAuth();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> create(@RequestParam int vacancyId) {
        log.info("create vote for employerId {}", vacancyId);
        Vote created = service.create(vacancyId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable(name = "id") int voteId, @RequestParam int vacancyId) {
        log.info("update voteId {} for vacancyId {}", voteId, vacancyId);
        service.update(voteId, vacancyId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("delete vote {}", id);
        service.delete(id);
    }

    @ApiIgnore
    @PostMapping( value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void setVote(@PathVariable(name = "id") int vacancyId, @RequestParam boolean toVote) {
        log.info(toVote ? "enable {}" : "disable {}", vacancyId);
        service.setVote(vacancyId, toVote);
    }
}
