package ua.training.top.web;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void getUsers() throws Exception {
        perform(get("/users")
//                .with(userAuth(admin))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"));
    }

/*
    @Test
    void unAuth() throws Exception {
        perform(get("/users"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
*/

    @Test
    void getVacancies() throws Exception {
        perform(get("/vacancies")
//                .with(userAuth(UserTestData.user))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("vacancies"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/vacancies.jsp"));
    }
}
