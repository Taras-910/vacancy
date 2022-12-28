package ua.training.top.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ua.training.testData.UserTestData;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.User;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.testData.TestUtil.NOT_FOUND;
import static ua.training.testData.UserTestData.*;
import static ua.training.top.model.Role.USER;

class UserServiceTest extends AbstractServiceTest {

    @Autowired
    private UserService service;

    @Test
    void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, user);
    }

    @Test
    void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    void createDuplicate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", USER)));
    }

    @Test
    void createInvalid(){
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, null, "mail@yandex.ru", "password", USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", USER)));
        validateRootCause(NullPointerException.class, () -> service.create(new User(null, "User", null, "password", USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", USER)));
        validateRootCause(IllegalArgumentException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", null, USER)));
    }

    @Test
    void update() {
        User updated = getUpdated();
        service.update(updated, ADMIN_ID);
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), getUpdated());
    }

    @Test
    void updateDuplicate() {
        User duplicate = new User(user);
        duplicate.setEmail(admin.getEmail());
        assertThrows(DataAccessException.class, () -> service.update(duplicate, USER_ID));
    }

    @Test
    void updateInvalid(){
        validateRootCause(ConstraintViolationException.class, () -> service.update(new User(null, null, "mail@yandex.ru", "password", USER), USER_ID));
        validateRootCause(ConstraintViolationException.class, () -> service.update(new User(null, "  ", "mail@yandex.ru", "password", USER), USER_ID));
        validateRootCause(NullPointerException.class, () -> service.update(new User(null, "user", null, "password", USER), USER_ID));
        validateRootCause(ConstraintViolationException.class, () -> service.update(new User(null, "user", "  ", "password", USER), USER_ID));
        validateRootCause(IllegalArgumentException.class, () -> service.update(new User(null, "user", "mail@yandex.ru", null, USER), USER_ID));
    }
}
