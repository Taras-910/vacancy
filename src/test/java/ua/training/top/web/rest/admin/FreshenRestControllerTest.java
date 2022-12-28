package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.testData.FreshenTestData;
import ua.training.testData.UserTestData;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Freshen;
import ua.training.top.service.FreshenService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.FreshenTestData.*;
import static ua.training.testData.TestUtil.*;
import static ua.training.testData.UserTestData.ADMIN_ID;
import static ua.training.testData.UserTestData.admin;
import static ua.training.top.util.exception.ErrorType.VALIDATION_ERROR;

class FreshenRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = FreshenRestController.REST_URL + '/';
    private static final Logger log = LoggerFactory.getLogger(FreshenRestControllerTest.class);
    @Autowired
    private FreshenService service;

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FRESHEN1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FRESHEN_MATCHER.contentJson(freshen1));
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
        Iterable<Freshen> iterable = List.of(freshen1, freshen2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FRESHEN_MATCHER.contentJson(iterable));
    }

    @Test
    void create() throws Exception {
        Freshen newFreshen = new Freshen(FreshenTestData.getNew());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(newFreshen)))
                .andExpect(status().isCreated());
        Freshen created = readFromJson(action, Freshen.class);
        newFreshen.setId(created.getId());
        FRESHEN_MATCHER.assertMatch(created, newFreshen);
        FRESHEN_MATCHER.assertMatch(service.get(created.getId()), newFreshen);
    }

    @Test
    void createInvalid() throws Exception {
        Freshen invalid = new Freshen(freshen1);
        invalid.setWorkplace("n".repeat(101));
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void createForbidden() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(UserTestData.admin)))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + FRESHEN1_ID)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(FRESHEN1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        Freshen updated = new Freshen(FreshenTestData.getUpdated());
        perform(MockMvcRequestBuilders.put(REST_URL + FRESHEN1_ID)
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        FRESHEN_MATCHER.assertMatch(service.get(FRESHEN1_ID), updated);
    }
}
