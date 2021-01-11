package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.top.aggregator.AggregatorController;
import ua.training.top.model.Freshen;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.VacancyUtil.getResult;

@RestController
@RequestMapping(value = "profile/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
public class FreshenUIController {
    public static final Logger log = LoggerFactory.getLogger(FreshenUIController.class);

    @Autowired
    private AggregatorController controller;


    @GetMapping
    public Freshen getNewFreshen() {
        log.info("getNewFreshen by user {}", authUserId());
        return new Freshen(null, LocalDateTime.now(), null, null, authUserId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refreshDB(@Valid Freshen freshen, BindingResult result) {
        log.info("refreshDB freshen {}", freshen);
        if (result.hasErrors()) {
            getResult(result);
        }
        controller.refreshDB(freshen);
    }
}
