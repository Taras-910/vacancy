package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Vote;
import ua.training.top.service.VoteService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.TestUtil.*;
import static ua.training.testData.UserTestData.ADMIN_ID;
import static ua.training.testData.UserTestData.admin;
import static ua.training.testData.VacancyTestData.VACANCY2_ID;
import static ua.training.testData.VoteTestData.*;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.web.rest.admin.VoteRestController.REST_URL;

class VoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';
    @Autowired
    private VoteService service;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(vote1));
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + ADMIN_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll() throws Exception {
        Iterable<Vote> iterable = List.of(vote1, vote2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(iterable));
    }

    @Test
    void getAllAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "auth")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(vote1)));
    }

    @Test
    void create() throws Exception {
        Vote newVote = new Vote(null, LocalDate.now(), VACANCY2_ID, ADMIN_ID);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .param("vacancyId", String.valueOf(VACANCY2_ID))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isCreated());
        Vote created = readFromJson(action, Vote.class);
        newVote.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newVote);
        setTestAuthorizedUser(admin);
        VOTE_MATCHER.assertMatch(service.get(created.getId()), newVote);
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Vote updated = new Vote(vote1);
        updated.setVacancyId(VACANCY2_ID);
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + VOTE1_ID)
                .param("vacancyId", String.valueOf(VACANCY2_ID))
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        updated.setDateVote(LocalDate.now());
        VOTE_MATCHER.assertMatch(service.get(VOTE1_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + VOTE1_ID)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        setTestAuthorizedUser(admin);
        assertThrows(NotFoundException.class, () -> service.get(VOTE1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + NOT_FOUND)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void setVote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + VACANCY2_ID)
                        .param("toVote", String.valueOf(true))
                        .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isOk());
    }
}
