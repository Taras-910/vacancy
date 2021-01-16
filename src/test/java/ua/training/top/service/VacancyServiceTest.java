package ua.training.top.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ua.training.top.model.Employer;
import ua.training.top.model.User;
import ua.training.top.model.Vacancy;
import ua.training.top.testData.FreshenTestData;
import ua.training.top.testData.UserTestData;
import ua.training.top.testData.VacancyToTestData;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.ui.VacancyUIController;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.EmployerTestData.*;
import static ua.training.top.testData.FreshenTestData.FRESHEN2_ID;
import static ua.training.top.testData.UserTestData.NOT_FOUND;
import static ua.training.top.testData.VacancyTestData.*;
import static ua.training.top.testData.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.top.testData.VacancyToTestData.vacancyTo1;
import static ua.training.top.util.DateTimeUtil.DATE_TEST;
import static ua.training.top.util.VacancyUtil.fromTo;
import static ua.training.top.util.VacancyUtil.getEmployerFromTo;

public class VacancyServiceTest extends AbstractServiceTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VacancyUIController controller;
    @Autowired
    private VoteService voteService;
    @Autowired
    private EmployerService employerService;
    @Autowired
    private FreshenService freshenService;

    @Test
    public void get() throws Exception {
        Vacancy vacancy = vacancyService.get(VACANCY1_ID);
        VACANCY_MATCHER.assertMatch(vacancy, vacancy1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> vacancyService.get(NOT_FOUND));
    }

    @Test
    public void getAll() throws Exception {
        List<Vacancy> all = vacancyService.getAll();
        VACANCY_MATCHER.assertMatch(all, VACANCIES_GET_ALL);
    }

    @Test
    public void delete() throws Exception {
        vacancyService.delete(VACANCY2_ID);
        assertThrows(NotFoundException.class, () -> vacancyService.get(VACANCY2_ID));
    }

    @Test
    public void deleteNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> vacancyService.delete(NOT_FOUND));
    }

    @Test
    public void createDuplicate() throws Exception {
        VacancyTo created = new VacancyTo(vacancyTo1);
        created.setId(null);
        assertThrows(DataIntegrityViolationException.class, () -> vacancyService.createVacancyAndEmployer(created));
    }

    @Test
    public void createErrorData() throws Exception {
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, null, "Microsoft", "London", 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", null, "London", 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", null, 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net", null, DATE_TEST, "https://www.ukr.net/1/11","java", "киев", false)));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net", "Java Core", DATE_TEST, "https://www.ukr.net/1/11",null, "киев", false)));
    }

    @Test
    public void createListOfVacancies() throws Exception {
        List<Vacancy> actual = List.of(vacancy3, vacancy4);
        int freshenId = freshenService.create(FreshenTestData.getNew()).getId();
        List<Vacancy> created = vacancyService.createList(actual, EMPLOYER1_ID, freshenId);
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        VACANCY_MATCHER.assertMatch(created, actual);
    }

    @Test
    public void createListErrorData() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> vacancyService.createList(List.of(new Vacancy(vacancy1), new Vacancy(vacancy2)), EMPLOYER2_ID, FRESHEN2_ID));
        assertThrows(NullPointerException.class, () -> vacancyService.createList(List.of(null, new Vacancy(vacancy3)), EMPLOYER2_ID, FRESHEN2_ID));
        assertThrows(NullPointerException.class, () -> vacancyService.createList(null, EMPLOYER2_ID, FRESHEN2_ID));
        assertThrows(IllegalArgumentException.class, () -> vacancyService.createList(List.of(new Vacancy(vacancy4)), null, FRESHEN2_ID));
        assertThrows(IllegalArgumentException.class, () -> vacancyService.createList(List.of(new Vacancy(vacancy4)), EMPLOYER2_ID, null));
    }

    @Test
    public void update() throws Exception {
        VacancyTo vTo = VacancyToTestData.getUpdate();
        Vacancy updated = vacancyService.updateTo(vTo);
        VACANCY_MATCHER.assertMatch(fromTo(vTo), updated);
    }

    @Test
    public void updateErrorData() throws Exception {
        assertThrows(IllegalArgumentException.class, () -> vacancyService.updateTo(new VacancyTo()));
    }

    @Test
    public void createVacancyAndEmployer() throws Exception  {
        setTestAuthorizedUser(new User(UserTestData.admin));
        VacancyTo newVacancyTo = new VacancyTo(VacancyToTestData.getNew());
        Vacancy createdVacancy = vacancyService.createVacancyAndEmployer(newVacancyTo);
        int newIdVacancy = createdVacancy.id();
        newVacancyTo.setId(newIdVacancy);
        VACANCY_MATCHER.assertMatch(createdVacancy, fromTo(newVacancyTo));
        Employer newEmployer = getEmployerFromTo(newVacancyTo);
        Employer createdEmployer = employerService.getOrCreate(getEmployerFromTo(newVacancyTo));
        int newIdEmployer = createdEmployer.id();
        newEmployer.setId(newIdEmployer);
        EMPLOYER_MATCHER.assertMatch(createdEmployer, newEmployer);
        VACANCY_TO_MATCHER.assertMatch(VacancyUtil.getTo(vacancyService.get(newIdVacancy), voteService.getAllForAuth()), newVacancyTo);
    }

}
