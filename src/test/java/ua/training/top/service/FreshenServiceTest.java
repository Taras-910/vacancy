package ua.training.top.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.testData.FreshenTestData;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Freshen;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.testData.FreshenTestData.*;
import static ua.training.testData.TestUtil.NOT_FOUND;
import static ua.training.testData.UserTestData.admin;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;

class FreshenServiceTest extends AbstractServiceTest {

    @Autowired
    private FreshenService service;

    @Test
    void create() throws Exception {
        setTestAuthorizedUser(admin);
        Freshen created = service.create(FreshenTestData.getNew());
        int newId = created.id();
        Freshen newFreshen = FreshenTestData.getNew();
        newFreshen.setId(newId);
        FRESHEN_MATCHER.assertMatch(created, newFreshen);
        FRESHEN_MATCHER.assertMatch(service.get(newId), newFreshen);
    }

    @Test
    void delete() {
        service.delete(FRESHEN1_ID);
        assertThrows(NotFoundException.class, () -> service.get(FRESHEN1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Freshen freshen = service.get(FRESHEN1_ID);
        FRESHEN_MATCHER.assertMatch(freshen, freshen1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void update() {
        Freshen updated = FreshenTestData.getUpdated();
        service.update(updated, FRESHEN1_ID);
        FRESHEN_MATCHER.assertMatch(service.get(FRESHEN1_ID), FreshenTestData.getUpdated());
    }

    @Test
    void getAll() {
        List<Freshen> all = service.getAll();
        FRESHEN_MATCHER.assertMatch(all, freshen1, freshen2);
    }

}
