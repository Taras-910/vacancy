package ua.training.top.web.rest.admin;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.testData.RateTestData;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Rate;
import ua.training.top.service.RateService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.RateTestData.*;
import static ua.training.testData.TestUtil.*;
import static ua.training.testData.UserTestData.*;
import static ua.training.top.util.exception.ErrorType.VALIDATION_ERROR;

class RateRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RateRestController.REST_URL + '/';

    @Autowired
    private RateService service;
    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        cacheManager.getCache("rate").clear();
    }

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RATE1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATE_MATCHER.contentJson(rate1));
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
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll() throws Exception {
        Iterable<Rate> allRates = allRates();
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATE_MATCHER.contentJson(allRates));
    }

    @Test
    void create() throws Exception {
        Rate newRate = new Rate(RateTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(newRate)))
                .andExpect(status().isCreated());
        Rate created = readFromJson(action, Rate.class);
        newRate.setId(created.getId());
        RATE_MATCHER.assertMatch(created, newRate);
        RATE_MATCHER.assertMatch(service.get(created.getId()), newRate);
    }

    @Test
    void createInvalid() throws Exception {
        Rate invalid = new Rate(RateTestData.getNew());
        invalid.setName("n".repeat(7));
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RATE1_ID)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(RATE1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RATE1_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }
}
