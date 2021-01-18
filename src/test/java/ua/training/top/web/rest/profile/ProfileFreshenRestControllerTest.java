package ua.training.top.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.service.EmployerService;
import ua.training.top.service.FreshenService;
import ua.training.top.service.VacancyService;
import ua.training.top.web.AbstractControllerTest;
import ua.training.top.web.json.JsonUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.top.aggregator.strategy.TestStrategy.getTestList;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.setJUnitTest;
import static ua.training.top.testData.FreshenTestData.*;
import static ua.training.top.testData.TestUtil.userHttpBasic;
import static ua.training.top.testData.UserTestData.user;
import static ua.training.top.util.VacancyUtil.fromTos;
import static ua.training.top.util.VacancyUtil.getEmployersFromTos;


class ProfileFreshenRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = ProfileFreshenRestController.REST_URL + '/';
    @Autowired
    private FreshenService freshenService;
    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private EmployerService employerService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + FRESHEN2_ID)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FRESHEN_MATCHER.contentJson(freshen2));
    }

    @Test
    void getOwn() throws Exception {
        Iterable<Freshen> freshens = List.of(freshen2);
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FRESHEN_MATCHER.contentJson(freshens));
    }

    @Test
    void refreshDB() throws Exception  {
        Freshen freshen = new Freshen(null, null, "Java", "Киев", null );
        List<Vacancy> vacanciesDbBefore = vacancyService.getAll();
        List<Freshen> freshensDbBefore = freshenService.getAll();
        List<Employer> employersDbBefore = employerService.getAll();
        setJUnitTest();

        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(freshen)))
                .andDo(print())
                .andExpect(status().isNoContent());
        List<Freshen> allFreshens = freshenService.getAll();
        Freshen newFreshen = allFreshens.stream()
                .filter(f -> !freshensDbBefore.contains(f)).collect(Collectors.toList()).get(0);
        freshen.setRecordedDate(newFreshen.getRecordedDate());
        freshen.setUserId(newFreshen.getUserId());
        freshen.setId(newFreshen.getId());
        FRESHEN_MATCHER.assertMatch(newFreshen, freshen);

        List<Vacancy> vacanciesTest = fromTos(getTestList());
        List<Vacancy> allVacancies = vacancyService.getAll();
        List<Vacancy> newVacancies = allVacancies.stream()
                .filter(v -> !vacanciesDbBefore.contains(v)).collect(Collectors.toList());
        assertTrue(vacanciesTest.stream()
                .filter(v -> !newVacancies.contains(v)).collect(Collectors.toList()).size() == 0);

        Set<Employer> employersTest = new HashSet<>(getEmployersFromTos(getTestList()));
        List<Employer> allEmployers = employerService.getAll();
        List<Employer> newEmployers = allEmployers.stream()
                .filter(e -> !employersDbBefore.contains(e)).collect(Collectors.toList());
        assertTrue(employersTest.stream()
                .filter(v -> !newEmployers.contains(v)).collect(Collectors.toList()).size() == 0);
    }
}

