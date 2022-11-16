package ua.training.top.web.rest.admin;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.model.Rate;
import ua.training.top.service.RateService;
import ua.training.top.testData.RateTestData;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.testData.RateTestData.*;
import static ua.training.top.testData.TestUtil.readFromJson;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.admin;

public class RateRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RateRestController.REST_URL + '/';
    private static final Logger log = LoggerFactory.getLogger(RateRestControllerTest.class);

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
    public void getAll() throws Exception {
        Iterable<Rate> allRates = allRates();
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RATE_MATCHER.contentJson(allRates));
    }

    @Test
    public void create() throws Exception {
        Rate newRate = new Rate(RateTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(JsonUtil.writeValue(newRate)))
                .andExpect(status().isCreated());
        Rate created = readFromJson(action, Rate.class);
        newRate.setId(created.getId());
        RATE_MATCHER.assertMatch(created, newRate);
        RATE_MATCHER.assertMatch(service.get(created.getId()), newRate);
    }

    @Test
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + RATE1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(RATE1_ID));
    }
}
