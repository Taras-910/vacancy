package ua.training.top.web.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import ua.training.top.model.User;
import ua.training.top.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.MessageUtil.email_error;
import static ua.training.top.util.MessageUtil.email_matcher;

@ApiIgnore
@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController {
    public static final Logger log = LoggerFactory.getLogger(AdminUIController.class);
    @Autowired
    UserService service;

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createOrUpdate(@Valid User user) {
        log.info("createOrUpdate {}", user);
        if(!user.getEmail().matches(email_matcher)) {
            throw new IllegalArgumentException(email_error);
        }
        if (user.isNew()) {
            service.create(user);
        } else {
            service.update(user, user.id());
        }
    }

    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        service.enable(id, enabled);
    }
}

