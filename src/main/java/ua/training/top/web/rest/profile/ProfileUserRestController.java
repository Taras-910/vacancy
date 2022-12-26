package ua.training.top.web.rest.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ua.training.top.model.User;
import ua.training.top.service.UserService;
import ua.training.top.web.AbstractUserController;

import javax.validation.Valid;

import static ua.training.top.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileUserRestController.REST_URL)
public class ProfileUserRestController extends AbstractUserController {
    static final String REST_URL = "/rest/profile/users";

    @Autowired
    UserService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return service.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        service.delete(authUserId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody User user) {
        service.update(user, authUserId());
    }
}
