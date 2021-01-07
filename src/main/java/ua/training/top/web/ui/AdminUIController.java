package ua.training.top.web.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.top.model.User;
import ua.training.top.service.UserService;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getResult;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController {
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
    public void createOrUpdate(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            getResult(result);
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
