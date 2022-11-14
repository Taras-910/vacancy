package ua.training.top.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.AbstractTest;
import ua.training.top.model.Rate;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.top.testData.RateTestData.*;
import static ua.training.top.testData.TestUtil.NOT_FOUND;

public class RateServiceTest extends AbstractTest {

    @Autowired
    RateService service;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void clearCache() {
        cacheManager.getCache("rate").clear();
    }

    @Test
    public void get() throws Exception {
        Rate rate = service.get(RATE1_ID);
        RATE_MATCHER.assertMatch(rate1, rate);
    }

    @Test
    public void getNotValid() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        List<Rate> all = service.getAll();
        RATE_MATCHER.assertMatch(allRates(), all);
    }

    @Test
    @Transactional
    public void create() throws Exception {
        Rate testRate = new Rate(getNew());
        Rate createdRate = service.create(testRate);
        int newId = createdRate.id();
        testRate.setId(newId);
        RATE_MATCHER.assertMatch(testRate, createdRate);
    }

    @Test
    @Transactional
    public void delete() throws Exception {
        service.delete(RATE1_ID);
        assertThrows(NotFoundException.class, () -> service.get(RATE1_ID));
    }

    @Test
    public void deleteNotValid() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    @Transactional
    public void deleteAll() throws Exception {
        service.deleteAll();
        List<Rate> all = service.getAll();
        assertEquals(0, all.size());
    }

    @Test
    public void getByName() {
        Rate rate = service.getByName(rate10.getName());
        RATE_MATCHER.assertMatch(rate, rate10);
    }

    @Test
    public void getByNotFoundName() throws Exception {
        assertThrows(NotFoundException.class, () -> service.getByName("notFoundName"));
    }
}
