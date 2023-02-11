package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;

import javax.validation.Valid;

import static ua.training.top.util.FreshenUtil.asNewFreshen;

@ApiIgnore
@RestController
@RequestMapping(value = "/profile/freshen", produces = MediaType.APPLICATION_JSON_VALUE)
public class FreshenUIController {
    public static final Logger log = LoggerFactory.getLogger(FreshenUIController.class);

    private final FreshenService service;

    public FreshenUIController(FreshenService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Valid Freshen freshen) {
        log.info("refreshDB freshen {}", freshen);
        service.refreshDB(asNewFreshen(freshen));
    }
}
