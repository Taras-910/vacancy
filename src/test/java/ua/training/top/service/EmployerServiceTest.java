package ua.training.top.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Employer;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.dataTest.EmployerTestData.*;
import static ua.training.top.dataTest.UserTestData.NOT_FOUND;

public class EmployerServiceTest extends AbstractServiceTest {

    @Autowired
    EmployerService service;

    @Test
    public void getById() {
        Assert.assertEquals(service.getById(EMPLOYER1_ID), EMPLOYER1);
    }

    @Test
    public void getAll() {
        List<Employer> all = service.getAll();
        EMPLOYER_MATCHER.assertMatch(all, EMPLOYER1, EMPLOYER2);
    }

    @Test
    public void getByName() {
        EMPLOYER_MATCHER.assertMatch(service.getByName("RedLab"), EMPLOYER2);
    }

    @Test
    public void getErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> service.getById(NOT_FOUND));
    }

    @Test
    public void update() {
        Employer updated = getUpdated();
        service.update(updated);
        EMPLOYER_MATCHER.assertMatch(service.getById(EMPLOYER1_ID), getUpdated());
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(null));
        assertThrows(NotFoundException.class, () -> service.update(new Employer(EMPLOYER1_ID, null, "newAddress", "https://grc.ua")));
        assertThrows(NotFoundException.class, () -> service.update(new Employer(EMPLOYER1_ID, "Новый", null, "https://grc.ua")));
    }

    @Test
    public void create() {
        Employer newRestaurant = getNew();
        Employer created = service.create(newRestaurant);
        newRestaurant.setId(created.getId());
        EMPLOYER_MATCHER.assertMatch(created, newRestaurant);
    }

    @Test
    public void createErrorDate() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(new Employer(null, null, "newAddress", "https://grc.ua")));
    }

    @Test
    public void delete() {
        service.delete(EMPLOYER1_ID);
        assertThrows(NotFoundException.class, () -> service.getById(EMPLOYER1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }
}
