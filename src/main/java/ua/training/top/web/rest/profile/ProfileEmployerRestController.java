package ua.training.top.web.rest.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.training.top.model.Employer;
import ua.training.top.service.EmployerService;
import ua.training.top.web.rest.admin.EmployerRestController;

import java.util.List;

@RestController
@RequestMapping(value = ProfileEmployerRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileEmployerRestController {
    static final String REST_URL = "/rest/profile/employers";
    public static final Logger log = LoggerFactory.getLogger(EmployerRestController.class);
    private final EmployerService service;

    public ProfileEmployerRestController(EmployerService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Employer get(@PathVariable int id) {
        return service.get(id);
    }

    @GetMapping
    public List<Employer> getAll() {
        return service.getAll();
    }
}
