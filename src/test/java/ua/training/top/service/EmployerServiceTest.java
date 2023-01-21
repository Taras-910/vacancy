package ua.training.top.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.testData.EmployerTestData;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Employer;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.testData.EmployerTestData.*;
import static ua.training.testData.TestUtil.NOT_FOUND;

class EmployerServiceTest extends AbstractServiceTest {

    @Autowired
    EmployerService service;

    @Test
    public void get() {
        Assertions.assertEquals(service.get(EMPLOYER1_ID), employer1);
    }

    @Test
    public void getAll() {
        List<Employer> all = service.getAll();
        EMPLOYER_MATCHER.assertMatch(all, employer1, employer2);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void create() {
        Employer newEmployer = EmployerTestData.getNew();
        Employer created = service.create(newEmployer);
        newEmployer.setId(created.getId());
        EMPLOYER_MATCHER.assertMatch(created, newEmployer);
    }

    @Test
    void createInvalid(){
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Employer(null, "   ", "Sity")));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Employer(null, null, "Sity")));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Employer(null, "newName", "   ")));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Employer(null, "newName", null)));
    }

    @Test
    public void update() {
        Employer updated = new Employer(getUpdated());
        service.update(updated);
        EMPLOYER_MATCHER.assertMatch(service.get(EMPLOYER1_ID), updated);
    }

    @Test
    public void updateInvalid() {
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
        assertThrows(IllegalArgumentException.class, () -> service.update(new Employer(EMPLOYER1_ID, null, "newAddress")));
        assertThrows(IllegalArgumentException.class, () -> service.update(new Employer(EMPLOYER1_ID, "Новый", null)));
    }

    @Test
    public void delete() {
        service.delete(EMPLOYER1_ID);
        assertThrows(NotFoundException.class, () -> service.get(EMPLOYER1_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

}
