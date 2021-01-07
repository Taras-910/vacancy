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
import ua.training.top.dataTest.VacancyTestData;
import ua.training.top.dataTest.VacancyToTestData;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.service.EmployerService;
import ua.training.top.service.VacancyService;
import ua.training.top.service.VoteService;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.dataTest.EmployerTestData.EMPLOYER_MATCHER;
import static ua.training.top.dataTest.TestUtil.readFromJson;
import static ua.training.top.dataTest.VacancyTestData.*;
import static ua.training.top.dataTest.VacancyToTestData.VACANCY_TO_MATCHER;
import static ua.training.top.dataTest.VacancyToTestData.vacancyTo1;
import static ua.training.top.util.VacancyUtil.*;
import static ua.training.top.util.jsoup.EmployerUtil.getEmployerFromTo;

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
//                .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(createTo(vacancy1, voteService.getAllForAuthUser())));
    }

/*
    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND)
//                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
*/

    /*@Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VACANCY1_ID))
                .andExpect(status().isUnauthorized());
    }*/

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
//                .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2, vacancy1), voteService.getAllForAuthUser())));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + VACANCY1_ID)
//                .with(userHttpBasic(user))
        )
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> vacancyService.get(VACANCY1_ID));
    }

/*
    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
//                .with(userHttpBasic(user))
        )
                .andExpect(status().isBadRequest());
    }
*/

    @Test
    void update() throws Exception {
        VacancyTo updated = VacancyTestData.getUpdated();
        perform(MockMvcRequestBuilders
                        .put(REST_URL + VACANCY1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(updated))
//                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(status().isNoContent());
        VACANCY_MATCHER.assertMatch(vacancyService.get(VACANCY1_ID), getVacancyFromTo(updated));
    }

    @Test
    void updateInvalid() throws Exception {
        VacancyTo invalid = new VacancyTo(VacancyTestData.getUpdated());
        invalid.setEmployerName(null);
        perform(MockMvcRequestBuilders.put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
 //               .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        VacancyTo invalid = new VacancyTo(vacancyTo1);
        perform(MockMvcRequestBuilders.put(REST_URL + VACANCY1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid))
//                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createVacancyEmployer() throws Exception {
        VacancyTo newVacancyTo = VacancyToTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(newVacancyTo))
//                .with(userHttpBasic(user))
        );
        Vacancy createdVacancy = readFromJson(action, Vacancy.class);
        int newIdVacancy = createdVacancy.id();
        newVacancyTo.setId(newIdVacancy);
        VACANCY_MATCHER.assertMatch(createdVacancy, getVacancyFromTo(newVacancyTo));
        Employer newEmployer = getEmployerFromTo(newVacancyTo);
        Employer createdEmployer = employerService.getOrCreate(getEmployerFromTo(newVacancyTo));
        int newIdEmployer = createdEmployer.id();
        newEmployer.setId(newIdEmployer);
        EMPLOYER_MATCHER.assertMatch(createdEmployer, newEmployer);
        VACANCY_TO_MATCHER.assertMatch(createTo(vacancyService.get(newIdVacancy), voteService.getAllForAuthUser()), newVacancyTo);
    }

    @Test
    void createInvalid() throws Exception {
        VacancyTo invalid = new VacancyTo(VacancyToTestData.getNew());
        invalid.setTitle(null);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(invalid))
//                .with(userHttpBasic(user))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception {
        VacancyTo duplicate = new VacancyTo(vacancyTo1);
        vacancyTo1.setId(null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate))
//                .with(userHttpBasic(admin))
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByFilter() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                        .param("language", "java").param("workplace", "киев")
                //               .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2), voteService.getAllForAuthUser())));
    }

    @Test
    void getByFilterAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                        .param("language", "").param("workplace", "")
                //               .with(userHttpBasic(user))
        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(VACANCY_TO_MATCHER.contentJson(getTos(List.of(vacancy2, vacancy1), voteService.getAllForAuthUser())));
    }

    @Test
    void setVote() throws Exception {
        Vote vote = voteService.getAllForAuthUser().stream().filter(v -> v.getVacancyId()== VACANCY2_ID).findFirst().orElse(null);
        assertTrue(vote == null);
        perform(MockMvcRequestBuilders.post(REST_URL + VACANCY2_ID)
                .param("enabled", "true")
//                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
        vote = voteService.getAllForAuthUser().stream().filter(v -> v.getVacancyId()== VACANCY2_ID).findFirst().orElse(null);
        assertTrue(vote != null);
    }
}
