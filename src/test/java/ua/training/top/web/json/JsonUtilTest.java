package ua.training.top.web.json;

import org.junit.jupiter.api.Test;
import ua.training.top.model.User;
import ua.training.top.model.Vacancy;
import ua.training.top.testData.UserTestData;
import ua.training.top.testData.VacancyTestData;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ua.training.top.testData.VacancyTestData.*;

class JsonUtilTest {

    @Test
    void readWriteValue() throws Exception {
        String json = JsonUtil.writeValue(vacancy1);
        System.out.println(json);
        Vacancy vacancy = JsonUtil.readValue(json, Vacancy.class);
        VACANCY_MATCHER.assertMatch(vacancy, vacancy1);
    }

    @Test
    void readWriteValues() throws Exception {
        String json = JsonUtil.writeValue(getListVacancies());
        System.out.println(json);
        List<Vacancy> meals = JsonUtil.readValues(json, Vacancy.class);
        VACANCY_MATCHER.assertMatch(meals, VacancyTestData.getListVacancies());
    }

    @Test
    void writeOnlyAccess() throws Exception {
        String json = JsonUtil.writeValue(UserTestData.user);
        System.out.println(json);
        assertThat(json, containsString("password"));
        String jsonWithPass = UserTestData.jsonWithPassword(UserTestData.user, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(user.getPassword(), "newPass");
    }
}
