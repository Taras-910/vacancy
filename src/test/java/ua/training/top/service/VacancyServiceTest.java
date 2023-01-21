package ua.training.top.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.training.testData.VacancyToTestData;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.testData.EmployerTestData.*;
import static ua.training.testData.FreshenTestData.freshen1;
import static ua.training.testData.TestUtil.NOT_FOUND;
import static ua.training.testData.UserTestData.admin;
import static ua.training.testData.VacancyTestData.*;
import static ua.training.testData.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.util.DateTimeUtil.testDate;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;
import static ua.training.top.util.VacancyUtil.fromTo;

class VacancyServiceTest extends AbstractServiceTest {

    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private EmployerService employerService;

    @Test
    void get() {
        Vacancy vacancy = vacancyService.get(VACANCY1_ID);
        Assertions.assertEquals(vacancy, vacancy1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> vacancyService.get(NOT_FOUND));
    }

    @Test
    void getAll() {
        List<Vacancy> all = vacancyService.getAll();
        VACANCY_MATCHER.assertMatch(all, VACANCIES_GET_ALL);
    }

    @Test
    void getByParams() {
        Vacancy vacancy = new Vacancy(vacancy1);
        Vacancy vacancyDb = vacancyService.getByParams(vacancy1.getTitle(), vacancy1.getSkills(), EMPLOYER1_ID);
        VACANCY_MATCHER.assertMatch(vacancy, vacancyDb);
    }

    @Test
    void createListVacancies() {
        setTestAuthorizedUser(admin);
        List<Vacancy> actual = List.of(vacancy3, vacancy4);
        actual.forEach(v -> {
            v.setEmployer(employer1);
            v.setFreshen(freshen1);
        });
        List<Vacancy> created = vacancyService.createUpdateList(actual);
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        VACANCY_MATCHER.assertMatch(created, actual);
    }

    @Test
    void createListVacanciesInvalid() {
        assertThrows(NullPointerException.class, () -> vacancyService.createUpdateList(List.of(null, new Vacancy(vacancy3))));
        assertThrows(NullPointerException.class, () -> vacancyService.createUpdateList(null));
    }

    @Test
    @Transactional
    void createVacancyAndEmployer() {
        setTestAuthorizedUser(admin);
        VacancyTo newVacancyTo = new VacancyTo(VacancyToTestData.getNew());
        Vacancy createdVacancy = vacancyService.createVacancyAndEmployer(newVacancyTo, getFreshenFromTo(newVacancyTo));
        int newIdVacancy = createdVacancy.id();
        newVacancyTo.setId(newIdVacancy);
        VACANCY_MATCHER.assertMatch(createdVacancy, fromTo(newVacancyTo));
        Employer newEmployer = getEmployerFromTo(newVacancyTo);
        Employer createdEmployer = employerService.getOrCreate(getEmployerFromTo(newVacancyTo));
        int newIdEmployer = createdEmployer.id();
        newEmployer.setId(newIdEmployer);
        EMPLOYER_MATCHER.assertMatch(createdEmployer, newEmployer);
        VACANCY_TO_MATCHER.assertMatch(VacancyUtil.getTo(vacancyService.get(newIdVacancy),
                voteService.getAllAuth()), newVacancyTo);
    }

    @Test
    void createVacancyAndEmployerInvalid(){
        setTestAuthorizedUser(admin);
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "   ", "employerName", "address", 100000, 200000, "https://aaa.ua", "skills…", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "title", "   ", "address", 100000, 200000, "https://aaa.ua", "skills…", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "title", "employerName", "   ", 100000, 200000, "https://aaa.ua", "skills…", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "title", "employerName", "address", 0, 200000, "https://aaa.ua", "skills…", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "title", "employerName", "address", 100000, 0, "https://aaa.ua", "skills…", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "title", "employerName", "address", 100000, 200000, "   ", "skills…", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(ConstraintViolationException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "title", "employerName", "address", 100000, 200000, "https://aaa.ua", "   ", testDate, "java", "middle", "киев", true), freshen1));
        validateRootCause(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, null, "Microsoft", "London", 100, 110, "https://www.ukr.net/1/11","Java Core", testDate, "java", "middle","киев", false), freshen1));
        validateRootCause(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", null, "London", 100, 110, "https://www.ukr.net/1/11","Java Core", testDate, "java", "middle","киев", false), freshen1));
        validateRootCause(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", null, 100, 110, "https://www.ukr.net/1/11", "Java Core", testDate, "java", "middle","киев", false), freshen1));
        validateRootCause(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net/1/11", null, testDate, "java", "middle","киев", false), freshen1));
    }

    @Test
    void update() {
        VacancyTo vTo = VacancyToTestData.getUpdate();
        Vacancy updated = vacancyService.updateTo(vTo);
        VACANCY_MATCHER.assertMatch(fromTo(vTo), updated);
    }

    @Test
    void updateInvalid() {
        assertThrows(IllegalArgumentException.class, () -> vacancyService.updateTo(new VacancyTo()));
    }

    @Test
    void delete() {
        vacancyService.delete(VACANCY2_ID);
        assertThrows(NotFoundException.class, () -> vacancyService.get(VACANCY2_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> vacancyService.delete(NOT_FOUND));
    }
}
