package ua.training.top.web;

import org.junit.jupiter.api.Test;
import ua.training.testData.UserTestData;
import ua.training.top.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.training.testData.TestUtil.userAuth;
import static ua.training.testData.UserTestData.admin;

public class RootControllerTest extends AbstractControllerTest {

    public RootControllerTest() {
    }

    @Test
    public void getUsers() throws Exception {
        perform(get("/users")
                .with(userAuth(admin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

    @Test
    public void unAuth() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void getVacancies() throws Exception {
        perform(get("/vacancies")
                .with(userAuth(UserTestData.user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/vacancies.jsp"));
    }
}
