package ua.training.top.web.ui;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.training.top.model.User;
import ua.training.top.web.AbstractUserController;

import javax.validation.Valid;
import java.util.List;

import static ua.training.top.util.VacancyUtil.getResult;

@RestController
@RequestMapping(value = "/admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminUIController extends AbstractUserController {

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable int id) {
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> createOrUpdate(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            getResult(result);
        }
        if (user.isNew()) {
            super.create(user);
        } else {
            super.update(user, user.id());
        }
        return ResponseEntity.ok().build();
    }

    @Override
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) {
        super.enable(id, enabled);
    }
}