package ua.training.top.web.jsp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ua.training.top.model.Employer;
import ua.training.top.service.EmployerService;
import ua.training.top.util.ValidationUtil;

import java.util.List;

import static ua.training.top.util.ValidationUtil.assureIdConsistent;
import static ua.training.top.util.ValidationUtil.checkNew;

@Controller
public class EmployerController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    EmployerService service;

    public List<Employer> getAll() {
        log.info("getAll");
        return service.getAll();
    }

    public Employer getById(int id) {
        log.info("get {}", id);
        return service.getById(id);
    }

    public Employer getByName(String name) {
        return service.getByName(name);
    }

    public List<Employer> getAllWithVacancies() {
        return service.getAllWithVacancies();
    }

    public Employer create(Employer employer) {
        log.info("create {}", employer);
        checkNew(employer);
        return service.create(employer);
    }

    public List<Employer> createAll(List<Employer> employers) {
        log.info("createAll");
        employers.forEach(ValidationUtil::checkNew);
        return service.createAll(employers);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Employer user, int id) {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        service.update(user);
    }

    public void deleteAll() {
        log.info("deleteAll");
        service.deleteAll();
    }
}
