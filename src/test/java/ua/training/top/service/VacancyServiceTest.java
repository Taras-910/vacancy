package ua.training.top.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.TransactionSystemException;
import ua.training.top.AbstractServiceTest;
import ua.training.top.dataTest.VacancyTestData;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.dataTest.EmployerTestData.EMPLOYER1_ID;
import static ua.training.top.dataTest.EmployerTestData.EMPLOYER2_ID;
import static ua.training.top.dataTest.UserTestData.NOT_FOUND;
import static ua.training.top.dataTest.VacancyTestData.*;
import static ua.training.top.util.DateTimeUtil.DATE_TEST;
import static ua.training.top.util.DateTimeUtil.LOCAL_DATE_TIME_TEST;
import static ua.training.top.util.VacancyUtil.getVacancyFromTo;

public class VacancyServiceTest extends AbstractServiceTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private VacancyService service;

    @Test
    public void get() throws Exception {
        Vacancy vacancy = service.get(VACANCY1_ID);
        VACANCY_MATCHER.assertMatch(vacancy, VACANCY1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        List<Vacancy> all = service.getAll();
        VACANCY_MATCHER.assertMatch(all, VACANCIES_GET_ALL);
    }

    @Test
    public void delete() throws Exception {
        service.delete(VACANCY2_ID);
        assertThrows(NotFoundException.class, () -> service.get(VACANCY2_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }


    @Test
    public void deleteEmployerVacancies() throws Exception {
        service.deleteEmployerVacancies(EMPLOYER2_ID);
        assertThrows(NotFoundException.class, () -> service.get(VACANCY2_ID));
    }

    @Test
    public void createDuplicate() throws Exception {
        Vacancy created = new Vacancy(VACANCY1);
        created.setId(null);
        assertThrows(DataIntegrityViolationException.class, () -> service.createUpdate(created, EMPLOYER1_ID));
    }

    @Test
    public void createErrorData() throws Exception {
        assertThrows(TransactionSystemException.class, () -> service.createUpdate(new Vacancy(null, 100,110, "", "", DATE_TEST, "java", "киев", LOCAL_DATE_TIME_TEST), EMPLOYER1_ID));
        assertThrows(TransactionSystemException.class, () -> service.createUpdate(new Vacancy("Developer", -100,110, "", "", DATE_TEST, "java", "киев", LOCAL_DATE_TIME_TEST), EMPLOYER1_ID));
        assertThrows(DataIntegrityViolationException.class, () -> service.createUpdate(new Vacancy("Developer", 100,110, null, "", DATE_TEST, "java", "киев", LOCAL_DATE_TIME_TEST), EMPLOYER1_ID));
        assertThrows(DataIntegrityViolationException.class, () -> service.createUpdate(new Vacancy("Developer", 100,110, "", null, DATE_TEST, "java", "киев", LOCAL_DATE_TIME_TEST), EMPLOYER1_ID));
        assertThrows(DataIntegrityViolationException.class, () -> service.createUpdate(new Vacancy("Developer", 100,110, "", "", DATE_TEST, "java", "киев", LOCAL_DATE_TIME_TEST), NOT_FOUND));
    }

/*
    @Test
    public void createListOfVacancies() throws Exception {
        List<Vacancy> actual = VacancyTestData.getListVacancies();
        List<Vacancy> created = service.createAll(actual, EMPLOYER1_ID);
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        VACANCY_MATCHER.assertMatch(created, actual);
    }

    @Test
    public void createListErrorData() throws Exception {
        assertThrows(NotFoundException.class, () -> service.createAll(asList(new Vacancy(VACANCY1), new Vacancy(VACANCY2)), EMPLOYER2_ID));
        assertThrows(NotFoundException.class, () -> service.createAll(asList(null, new Vacancy(VACANCY3)), EMPLOYER2_ID));
        assertThrows(NotFoundException.class, () -> service.createAll(null, EMPLOYER2_ID));
        assertThrows(NotFoundException.class, () -> service.createAll(asList(new Vacancy(VACANCY3)), NOT_FOUND));
    }
*/

    @Test
    public void update() throws Exception {
        VacancyTo updated = VacancyTestData.getUpdated();
        service.update(updated);
        Vacancy vacancyDb = service.get(VACANCY1_ID);
        VACANCY_MATCHER.assertMatch(getVacancyFromTo(updated), vacancyDb);
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(NullPointerException.class, () -> service.update(null));
    }

    @Test
    public void createByToAndEmployerId() throws Exception  {
        VacancyTo newVacancyTo = VacancyTestData.getNew();
        Vacancy vacancyFromTo = getVacancyFromTo(newVacancyTo);
        Vacancy created = service.createUpdate(vacancyFromTo, EMPLOYER1_ID);
        int newId = created.id();
        vacancyFromTo.setId(newId);
        VACANCY_MATCHER.assertMatch(vacancyFromTo, created);
        VACANCY_MATCHER.assertMatch(vacancyFromTo, service.get(newId));
    }

    @Test
    public void createVacancyAndEmployer() throws Exception  {
    }
}
