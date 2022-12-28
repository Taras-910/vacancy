package ua.training.top.web.rest.admin;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Employer;
import ua.training.top.service.EmployerService;
import ua.training.top.util.exception.NotFoundException;
import ua.training.top.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.EmployerTestData.*;
import static ua.training.testData.TestUtil.*;
import static ua.training.testData.UserTestData.*;
import static ua.training.top.util.exception.ErrorType.VALIDATION_ERROR;

class EmployerRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = EmployerRestController.REST_URL + '/';
    private static final Logger log = LoggerFactory.getLogger(EmployerRestControllerTest.class);
    @Autowired
    private EmployerService service;
    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + EMPLOYER1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(EMPLOYER_MATCHER.contentJson(employer1));
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
        Iterable<Employer> iterable = List.of(employer1, employer2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(EMPLOYER_MATCHER.contentJson(iterable));
    }

    @Test
    void create() throws Exception {
        Employer newEmployer = new Employer(null, "newEmployer", "newAddress");
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(newEmployer)))
                .andExpect(status().isCreated());
        Employer created = readFromJson(action, Employer.class);
        newEmployer.setId(created.getId());
        EMPLOYER_MATCHER.assertMatch(created, newEmployer);
        EMPLOYER_MATCHER.assertMatch(service.get(created.getId()), newEmployer);
    }

    @Test
    void createInvalid() throws Exception {
        Employer invalid = new Employer(null, null, "");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(errorType(VALIDATION_ERROR));
    }

    @Test
    void update() throws Exception {
        Employer updated = new Employer(employer1);
        updated.setName("newNameEmployer");
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(admin)).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        EMPLOYER_MATCHER.assertMatch(service.get(EMPLOYER1_ID), updated);
    }

    @Test
    void updateInvalid() throws Exception {
        Employer invalid = new Employer(employer1);
        invalid.setName("");
        perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void updateForbidden() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + EMPLOYER1_ID)
                .with(userHttpBasic(admin)).with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(EMPLOYER1_ID));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND)
                .with(userHttpBasic(admin)).with(csrf()))
                .andExpect(status().isUnprocessableEntity());
    }
}
