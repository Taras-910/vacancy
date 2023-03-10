package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.model.Vacancy;
import ua.training.top.testData.FreshenTestData;
import ua.training.top.testData.VacancyToTestData;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.EmployerTestData.EMPLOYER1_ID;
import static ua.training.top.testData.EmployerTestData.EMPLOYER_MATCHER;
import static ua.training.top.testData.UserTestData.NOT_FOUND;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.VacancyTestData.*;
import static ua.training.top.testData.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.top.util.DateTimeUtil.testDate;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.FreshenUtil.getFreshenFromTo;
import static ua.training.top.util.VacancyUtil.fromTo;

public class VacancyServiceTest extends AbstractServiceTest {

    @Autowired
    private VacancyService vacancyService;
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
    public void getByParams() throws Exception {
        Vacancy vacancy = new Vacancy(vacancy1);
        Vacancy vacancyDb = vacancyService.getByParams(vacancy1.getTitle(), vacancy1.getSkills(), EMPLOYER1_ID);
        VACANCY_MATCHER.assertMatch(vacancy, vacancyDb);
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
    public void createErrorData() throws Exception {
        setTestAuthorizedUser(admin);
        Freshen freshen = asNewFreshen(new Freshen(null, null, "Java", "????????", Collections.singleton(Goal.UPGRADE), null));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, null, "Microsoft", "London", 100, 110, "https://www.ukr.net/1/11","Java Core", testDate, "java", "????????", false), freshen));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", null, "London", 100, 110, "https://www.ukr.net/1/11","Java Core", testDate, "java", "????????", false), freshen));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", null, 100, 110, "https://www.ukr.net/1/11", "Java Core", testDate, "java", "????????", false), freshen));
        assertThrows(NullPointerException.class, () -> vacancyService.createVacancyAndEmployer(new VacancyTo(null, "Developer", "Microsoft", "London", 100, 110, "https://www.ukr.net/1/11", null, testDate, "java", "????????", false), freshen));
    }

    @Test
    public void createListOfVacancies() throws Exception {
        setTestAuthorizedUser(admin);
        List<Vacancy> actual = List.of(vacancy3, vacancy4);
        int freshenId = freshenService.create(FreshenTestData.getNew()).getId();
        List<Vacancy> created = vacancyService.createUpdateList(actual);
        for(int i = 0; i < created.size(); i++) {
            actual.get(i).setId(created.get(i).getId());
        }
        VACANCY_MATCHER.assertMatch(created, actual);
    }

    @Test
    public void createListErrorData() throws Exception {
        assertThrows(NullPointerException.class, () -> vacancyService.createUpdateList(List.of(null, new Vacancy(vacancy3))));
        assertThrows(NullPointerException.class, () -> vacancyService.createUpdateList(null));
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
                voteService.getAllForAuth()), newVacancyTo);
    }
}
