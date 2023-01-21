package ua.training.top.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.AbstractControllerTest;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.TestUtil.NOT_FOUND;
import static ua.training.testData.TestUtil.userHttpBasic;
import static ua.training.testData.UserTestData.ADMIN_ID;
import static ua.training.testData.UserTestData.admin;
import static ua.training.testData.VacancyTestData.VACANCY1_ID;
import static ua.training.testData.VoteTestData.*;
import static ua.training.top.web.rest.profile.ProfileVoteRestController.REST_URL;

class ProfileVoteRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + '/';

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + VOTE1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
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
    void getAllAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + "auth")
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(vote1)));
    }

    @Test
    void setVote() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL_SLASH + VACANCY1_ID)
                .param("enabled", String.valueOf(true))
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
