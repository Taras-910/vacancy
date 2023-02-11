package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.AuthorizedUser;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.FreshenUtil.asNewFreshen;

@RestController
@RequestMapping(value = ProfileFreshenRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileFreshenRestController {
    static final String REST_URL = "/rest/profile/freshen";
    public static final Logger log = LoggerFactory.getLogger(ProfileFreshenRestController.class);

    private final FreshenService service;

    public ProfileFreshenRestController(FreshenService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Freshen get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping
    public List<Freshen> getAllAuth(@ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        return service.getAllAuth(authUser.getId());
    }

    @ApiIgnore
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Valid @RequestBody Freshen freshen) {
        service.refreshDB(asNewFreshen(freshen));
    }

}
