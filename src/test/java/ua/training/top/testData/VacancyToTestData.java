package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.to.VacancyTo;

public class VacancyToTestData {
    public static TestMatcher<VacancyTo> EMPLOYER_MATCHER = TestMatcher.usingFieldsComparator();

/*    public static VacancyTo getNew() {
        return new VacancyTo(EMPLOYER1_ID,"Microsoft", Arrays.asList(VACANCY1), false);
    }

    public static VacancyTo testMenu() {
        EMPLOYER1.setVacancies(Arrays.asList(VACANCY1));
        return new VacancyTo(EMPLOYER1.id(), EMPLOYER1.getName(), EMPLOYER1.getVacancies(), false);
    }

    public static List<VacancyTo> allVacancies() {
        EMPLOYER1.setVacancies(Arrays.asList(VACANCY1));
        EMPLOYER2.setVacancies(Arrays.asList(VACANCY2));
        VacancyTo VACANCY1 = new VacancyTo(EMPLOYER1.id(), EMPLOYER1.getName(), EMPLOYER1.getVacancies(), false);
        VacancyTo VACANCY2 = new VacancyTo(EMPLOYER2.id(), EMPLOYER2.getName(), EMPLOYER2.getVacancies(), false);
        return Arrays.asList(VACANCY1, VACANCY2);
    }*/
}
