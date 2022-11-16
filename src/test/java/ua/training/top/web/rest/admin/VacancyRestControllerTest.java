package ua.training.top.web.rest.admin;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.service.EmployerService;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.testData.VacancyTestData;
import ua.training.top.testData.VacancyToTestData;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.EmployerTestData.EMPLOYER_MATCHER;
import static ua.training.top.testData.TestUtil.*;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.VacancyTestData.*;
import static ua.training.top.testData.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.VacancyUtil.fromTo;
import static ua.training.top.util.VacancyUtil.getTos;

@Transactional
public class VacancyRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VacancyRestController.REST_URL + '/';
    private static final Logger log = LoggerFactory.getLogger(VacancyRestControllerTest.class);
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private EmployerService employerService;

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VACANCY1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(VacancyUtil.getTo(vacancy1, voteService.getAllForAuth())));
    }

    @Test
    public void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy1, vacancy2), voteService.getAllForAuth())));
    }

    @Test
    public void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void update() throws Exception {
        VacancyTo updated = new VacancyTo(VacancyTestData.getToUpdated());
        perform(MockMvcRequestBuilders
                .put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        VACANCY_MATCHER.assertMatch(fromTo(updated), vacancyService.get(VACANCY1_ID));
    }

    @Test
    @Transactional
    public void updateInvalid() throws Exception {
        VacancyTo invalid = new VacancyTo(VacancyTestData.getToUpdated());
        invalid.setEmployerName(null);
        perform(MockMvcRequestBuilders.put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    public void createVacancyAndEmployer() throws Exception {
        VacancyTo newVacancyTo = new VacancyTo(VacancyToTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVacancyTo)))
                .andDo(print())
                .andExpect(status().isCreated());
        Vacancy createdVacancy = readFromJson(action, Vacancy.class);
        int newIdVacancy = createdVacancy.id();
        newVacancyTo.setId(newIdVacancy);
        VACANCY_MATCHER.assertMatch(createdVacancy, fromTo(newVacancyTo));
        Employer newEmployer = getEmployerFromTo(newVacancyTo);
        Employer createdEmployer = employerService.getOrCreate(getEmployerFromTo(newVacancyTo));
        int newIdEmployer = createdEmployer.id();
        newEmployer.setId(newIdEmployer);
        EMPLOYER_MATCHER.assertMatch(createdEmployer, newEmployer);
        setTestAuthorizedUser(admin);
        VACANCY_TO_MATCHER.assertMatch(VacancyUtil.getTo(vacancyService.get(newIdVacancy),
                voteService.getAllForAuth()), newVacancyTo);
    }


    @Test
    @Transactional
    public void createInvalid() throws Exception {
        VacancyTo invalid = new VacancyTo(VacancyToTestData.getNew());
        invalid.setTitle(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getByFilter() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "java")
                .param("level", "middle")
                .param("workplace", "киев")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2), voteService.getAllForAuth())));
    }

    @Test
    public void getByFilterInvalid() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "")
                .param("workplace", "")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VACANCY1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VACANCY2_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> vacancyService.get(VACANCY2_ID));
    }
}
