package ua.training.top.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Rate;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.testData.RateTestData.*;
import static ua.training.testData.TestUtil.NOT_FOUND;

class RateServiceTest extends AbstractServiceTest {
    @Autowired
    RateService service;

    @Test
    void get() {
        Rate rate = service.get(RATE1_ID);
        RATE_MATCHER.assertMatch(rate1, rate);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Rate> all = service.getAll();
        RATE_MATCHER.assertMatch(allRates(), all);
    }

    @Test
    void getByName() {
        Rate rate = service.getByName(rate10.getName());
        RATE_MATCHER.assertMatch(rate, rate10);
    }

    @Test
    void getByNameInvalid() {
        assertThrows(NotFoundException.class, () -> service.getByName("NameInvalid"));
    }

    @Test
    void create() {
        Rate testRate = new Rate(getNew());
        Rate createdRate = service.create(testRate);
        int newId = createdRate.id();
        testRate.setId(newId);
        RATE_MATCHER.assertMatch(testRate, createdRate);
    }

    @Test
    void createInvalid(){
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Rate(null,"   ",0.897, now())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Rate(null,null,0.897, now())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Rate(null,"USDGBP",0.0, now())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new Rate(null,"USDGBP",0.897, null)));
    }

    @Test
    void delete() {
        service.delete(RATE1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RATE1_ID));
    }

    @Test
    void deleteInvalid() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void deleteAll() {
        service.deleteAll();
        List<Rate> all = service.getAll();
        assertEquals(0, all.size());
    }
}
