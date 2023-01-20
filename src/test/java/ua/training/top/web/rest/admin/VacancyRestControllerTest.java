package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ua.training.testData.VacancyTestData;
import ua.training.testData.VacancyToTestData;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.service.EmployerService;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.VacancyUtil;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.EmployerTestData.EMPLOYER_MATCHER;
import static ua.training.testData.TestUtil.*;
import static ua.training.testData.UserTestData.admin;
import static ua.training.testData.UserTestData.user;
import static ua.training.testData.VacancyTestData.*;
import static ua.training.testData.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.testData.VacancyToTestData.vacancyTo1;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.util.EmployerUtil.getEmployerFromTo;
import static ua.training.top.util.VacancyUtil.fromTo;
import static ua.training.top.util.VacancyUtil.getTos;

class VacancyRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VacancyRestController.REST_URL + '/';
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
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VACANCY1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getByFilter() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "java")
                .param("level", "middle")
                .param("workplace", "киев")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2), voteService.getAllForAuth())));
    }

    @Test
    void getByFilterInvalid() throws Exception {
        setTestAuthorizedUser(admin);
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("language", "")
                .param("workplace", "")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy1, vacancy2), voteService.getAllForAuth())));
    }

    @Test
    @Transactional
    void update() throws Exception {
        setTestAuthorizedUser(admin);
        VacancyTo updated = new VacancyTo(VacancyTestData.getToUpdated());
        perform(MockMvcRequestBuilders
                .put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        VACANCY_MATCHER.assertMatch(fromTo(updated), vacancyService.get(VACANCY1_ID));
    }

    @Test
    @Transactional
    void updateInvalid() throws Exception {
        VacancyTo invalid = new VacancyTo(VacancyTestData.getToUpdated());
        invalid.setEmployerName(null);
        perform(MockMvcRequestBuilders.put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    void createVacancyAndEmployer() throws Exception {
        VacancyTo newVacancyTo = new VacancyTo(VacancyToTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin)).with(csrf())
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
    void createInvalid() throws Exception {
        VacancyTo invalid = new VacancyTo(VacancyToTestData.getNew());
        invalid.setTitle(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VACANCY2_ID)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> vacancyService.get(VACANCY2_ID));
    }

    @Test
    @Transactional
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VACANCY2_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void updateHtmlUnsafe() throws Exception {
        VacancyTo invalid = new VacancyTo(vacancyTo1);
        invalid.setTitle(vacancyTo1.getTitle()+"<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        Vacancy vacancyDB = vacancyService.get(VACANCY1_ID);
        assertEquals(vacancyDB.getTitle(), vacancyTo1.getTitle());
    }

}
