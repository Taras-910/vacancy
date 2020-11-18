package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Vacancy;
import ua.training.top.testData.VacancyTestData;
import ua.training.top.util.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertThrows;
import static ua.training.top.testData.EmployerTestData.EMPLOYER1_ID;
import static ua.training.top.testData.EmployerTestData.EMPLOYER2_ID;
import static ua.training.top.testData.UserTestData.NOT_FOUND;
import static ua.training.top.testData.VacancyTestData.*;
import static ua.training.top.util.DateTimeUtil.DATE_TEST;

public class VacancyServiceTest extends AbstractServiceTest {

    @Autowired
    VacancyService service;

    @Test
    public void get() {
        Vacancy vacancy = service.get(VACANCY1_ID, EMPLOYER1_ID);
        VACANCY_MATCHER.assertMatch(vacancy, VACANCY1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, EMPLOYER2_ID));
    }

    @Test
    public void getNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(VACANCY1_ID, EMPLOYER2_ID));
    }

    @Test
    public void getAll() {
        List<Vacancy> all = service.getAll();
        VACANCY_MATCHER.assertMatch(all, VACANCY1, VACANCY2);
    }

    @Test
    public void delete() throws Exception {
        service.delete(VACANCY2_ID, EMPLOYER2_ID);
        assertThrows(NotFoundException.class, () -> service.get(VACANCY2_ID, EMPLOYER2_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, EMPLOYER2_ID));
    }

    @Test
    public void deleteNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(VACANCY1_ID, EMPLOYER2_ID));
    }


    @Test
    public void deleteListOfVacancies() {
        service.deleteListOfVacancies(EMPLOYER2_ID);
        assertThrows(NotFoundException.class, () -> service.get(VACANCY1_ID, EMPLOYER2_ID));
    }

    @Test
    public void create() throws Exception {
        Vacancy newVacancy = VacancyTestData.getNew();
        Vacancy created = service.create(newVacancy, EMPLOYER1_ID);
        int newId = created.id();
        newVacancy.setId(newId);
        VACANCY_MATCHER.assertMatch(newVacancy, created);
        VACANCY_MATCHER.assertMatch(newVacancy, service.get(newId, EMPLOYER1_ID));
    }

    @Test
    public void createDuplicate() throws Exception {
        Vacancy created = new Vacancy(VACANCY1);
        created.setId(null);
        assertThrows(DataIntegrityViolationException.class, () -> service.create(created, EMPLOYER1_ID));
    }

    @Test
    public void createErrorData() throws Exception {
        Vacancy vacancy = new Vacancy(VACANCY1_ID, "Created Developer", DATE_TEST, 110, "", "");
        assertThrows(IllegalArgumentException.class, () -> service.create(vacancy, EMPLOYER1_ID));
        assertThrows(TransactionSystemException.class, () -> service.create(new Vacancy(null, DATE_TEST, 110, "", ""), EMPLOYER1_ID));
        assertThrows(TransactionSystemException.class, () -> service.create(new Vacancy("Developer", null, 110, "", ""), EMPLOYER1_ID));
        assertThrows(TransactionSystemException.class, () -> service.create(new Vacancy("Developer", DATE_TEST, -110, "", ""), EMPLOYER1_ID));
        assertThrows(DataIntegrityViolationException.class, () -> service.create(new Vacancy("Developer", DATE_TEST, 110, null, ""), EMPLOYER1_ID));
        assertThrows(DataIntegrityViolationException.class, () -> service.create(new Vacancy("Developer", DATE_TEST, 110, "", null), EMPLOYER1_ID));
        assertThrows(DataIntegrityViolationException.class, () -> service.create(new Vacancy("Developer", DATE_TEST, 110, "", ""), NOT_FOUND));
    }

    @Test
    public void createListOfVacancies() throws Exception {
        List<Vacancy> actual = VacancyTestData.getListVacancies();
        List<Vacancy> created = service.createListOfVacancies(actual, EMPLOYER1_ID);
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        VACANCY_MATCHER.assertMatch(created, actual);
    }

    @Test
    public void createListErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> service.createListOfVacancies(asList(VACANCY1, VACANCY2), EMPLOYER2_ID));
        assertThrows(NotFoundException.class, () -> service.createListOfVacancies(asList(null, VACANCY3), EMPLOYER2_ID));
        assertThrows(EntityNotFoundException.class, () -> service.createListOfVacancies(asList(VACANCY3, VACANCY4), NOT_FOUND));
        assertThrows(NotFoundException.class, () -> service.createListOfVacancies(null, EMPLOYER2_ID));
    }

    @Test
    public void update() throws Exception {
        Vacancy updated = VacancyTestData.getUpdated();
        service.update(updated, EMPLOYER1_ID);
        VACANCY_MATCHER.assertMatch(service.get(VACANCY1_ID, EMPLOYER1_ID), updated);
    }
//        return new Vacancy(VACANCY1_ID, "update Developer", new Date(2020, 10, 26), 700, "xxx", "update knowledge");
    @Test
    public void updateNotOwn() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(VacancyTestData.getUpdated(), EMPLOYER2_ID));
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> service.update(null, EMPLOYER2_ID));
        assertThrows(NotFoundException.class, () -> service.update(VacancyTestData.getUpdated(), NOT_FOUND));
    }
}
