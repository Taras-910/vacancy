package ua.training.top.web.rest.profile;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ua.training.top.AbstractControllerTest;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.model.Vacancy;
import ua.training.top.service.EmployerService;
import ua.training.top.service.FreshenService;
import ua.training.top.service.VacancyService;
import ua.training.top.web.json.JsonUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ua.training.testData.FreshenTestData.*;
import static ua.training.testData.TestUtil.userHttpBasic;
import static ua.training.testData.UserTestData.*;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.InstallationUtil.setTestProvider;
import static ua.training.top.aggregator.InstallationUtil.setTestReasonPeriodToKeep;
import static ua.training.top.aggregator.strategies.TestStrategy.getTestList;
import static ua.training.top.util.EmployerUtil.getEmployersFromTos;
import static ua.training.top.util.FreshenUtil.asNewFreshen;
import static ua.training.top.util.VacancyUtil.fromTos;

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
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID))
                .andExpect(status().isUnauthorized());
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
        Freshen freshen = new Freshen(null, null, "Java", "middle", "Киев", Collections.singleton(Goal.UPGRADE),null );
        List<Vacancy> vacanciesDbBefore = vacancyService.getAll();
        List<Freshen> freshensDbBefore = freshenService.getAll();
        List<Employer> employersDbBefore = employerService.getAll();
        setTestProvider();
        setTestAuthorizedUser(admin);
        setTestReasonPeriodToKeep();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin)).with(csrf())
                .content(JsonUtil.writeValue(asNewFreshen(freshen))))
                .andDo(print())
                .andExpect(status().isNoContent());
        List<Freshen> allFreshens = freshenService.getAll();
        /*https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist*/
        freshensDbBefore.forEach(allFreshens::remove);
        Freshen newFreshen = allFreshens.get(0);
        freshen.setRecordedDate(newFreshen.getRecordedDate());
        freshen.setUserId(newFreshen.getUserId());
        freshen.setId(newFreshen.getId());
        FRESHEN_MATCHER.assertMatch(newFreshen, freshen);

        List<Vacancy> vacanciesTest = fromTos(getTestList());
        List<Vacancy> allVacancies = vacancyService.getAll();
        List<Vacancy> newVacancies = allVacancies.stream()
                .filter(v -> !vacanciesDbBefore.contains(v)).collect(Collectors.toList());
        assertEquals((int) vacanciesTest.stream()
                .filter(v -> !newVacancies.contains(v)).count(), 0);

        Set<Employer> employersTest = new HashSet<>(getEmployersFromTos(getTestList()));
        List<Employer> allEmployers = employerService.getAll();
        List<Employer> newEmployers = allEmployers.stream()
                .filter(e -> !employersDbBefore.contains(e)).collect(Collectors.toList());
        assertEquals((int) employersTest.stream()
                .filter(v -> !newEmployers.contains(v)).count(), 0);
    }
}

