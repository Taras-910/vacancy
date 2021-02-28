package ua.training.top.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.model.Employer;
import ua.training.top.testData.EmployerTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.testData.EmployerTestData.*;
import static ua.training.top.testData.UserTestData.NOT_FOUND;

public class EmployerServiceTest extends AbstractServiceTest {

    @Autowired
    EmployerService service;

    @Test
    public void getById() {
        Assert.assertEquals(service.get(EMPLOYER1_ID), employer1);
    }

    @Test
    public void getAll() {
        List<Employer> all = service.getAll();
        EMPLOYER_MATCHER.assertMatch(all, employer1, employer2);
    }

    @Test
    public void getErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() {
        Employer updated = new Employer(getUpdated());
        service.update(updated);
        EMPLOYER_MATCHER.assertMatch(service.get(EMPLOYER1_ID), updated);
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> service.update(null));
        assertThrows(IllegalArgumentException.class, () -> service.update(new Employer(EMPLOYER1_ID, null, "newAddress")));
        assertThrows(IllegalArgumentException.class, () -> service.update(new Employer(EMPLOYER1_ID, "Новый", null)));
    }

    @Test
    public void create() {
        Employer newEmployer = EmployerTestData.getNew();
        Employer created = service.create(newEmployer);
        newEmployer.setId(created.getId());
        EMPLOYER_MATCHER.assertMatch(created, newEmployer);
    }

    @Test
    public void createErrorDate() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> service.create(new Employer(null, null, "newAddress")));
    }

    @Test
    public void delete() {
        service.delete(EMPLOYER1_ID);
        assertThrows(NotFoundException.class, () -> service.get(EMPLOYER1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }
}
