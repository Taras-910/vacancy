package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.EmployerTestData.EMPLOYER_MATCHER;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.NOT_FOUND;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.VacancyTestData.*;
import static ua.training.top.testData.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.top.testData.VacancyToTestData.vacancyTo1;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.VacancyUtil.fromTo;
import static ua.training.top.util.VacancyUtil.getTos;

@Transactional
class VacancyRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VacancyRestController.REST_URL + '/';
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private VoteService voteService;
    @Autowired
    private EmployerService employerService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VACANCY1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(VacancyUtil.getTo(vacancy1, voteService.getAllForAuth())));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2, vacancy1), voteService.getAllForAuth())));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VACANCY1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> vacancyService.get(VACANCY1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void update() throws Exception {
        VacancyTo updated = new VacancyTo(VacancyTestData.getToUpdated());
        perform(MockMvcRequestBuilders
                        .put(REST_URL + VACANCY1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        VACANCY_MATCHER.assertMatch(vacancyService.get(VACANCY1_ID), fromTo(updated));
    }

    @Test
    @Transactional
    void updateInvalid() throws Exception {
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
    void createVacancyAndEmployer() throws Exception {
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
        VACANCY_TO_MATCHER.assertMatch(VacancyUtil.getTo(vacancyService.get(newIdVacancy), voteService.getAllForAuth()),
                newVacancyTo);
    }


    @Test
    @Transactional
    void createInvalid() throws Exception {
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
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        VacancyTo duplicate = new VacancyTo(vacancyTo1);
        vacancyTo1.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate))
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void getByFilter() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "java")
                .param("workplace", "киев")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy1), voteService.getAllForAuth())));
    }

    @Test
    void getByEmptyFilter() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "")
                .param("workplace", "")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2, vacancy1), voteService.getAllForAuth())));
    }

    @Test
    void setVote() throws Exception {
        Vote vote = voteService.getAllForAuth().stream().filter(v -> v.getVacancyId()== VACANCY2_ID).findFirst().orElse(null);
        assertTrue(vote == null);
        perform(MockMvcRequestBuilders.post(REST_URL + VACANCY2_ID)
                .param("enabled", "true")
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        vote = voteService.getAllForAuth().stream().filter(v -> v.getVacancyId()== VACANCY2_ID).findFirst().orElse(null);
        assertTrue(vote != null);
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VACANCY1_ID))
                .andExpect(status().isUnauthorized());
    }
}
