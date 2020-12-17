package ua.training.top.dataTest;

import ua.training.top.TestMatcher;
import ua.training.top.model.Employer;

import java.util.Arrays;
import java.util.List;

import static ua.training.top.dataTest.VacancyTestData.VACANCY1;
import static ua.training.top.dataTest.VacancyTestData.VACANCY2;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class EmployerTestData {
    public static TestMatcher<Employer> EMPLOYER_MATCHER = TestMatcher.usingFieldsComparator("vacancies");

    public static final int EMPLOYER1_ID = START_SEQ + 2;
    public static final int EMPLOYER2_ID = EMPLOYER1_ID + 1;
    public static final Employer EMPLOYER1 = new Employer(EMPLOYER1_ID, "Huuuge Games", "Киев", "https://grc.ua");
    public static final Employer EMPLOYER2 = new Employer(EMPLOYER2_ID, "RedLab", "Киев", "https://grc.ua");

    public static List<Employer> allWithVacancies() {
        EMPLOYER1.setVacancies(Arrays.asList(VACANCY1));
        EMPLOYER2.setVacancies(Arrays.asList(VACANCY2));
        return Arrays.asList(EMPLOYER1, EMPLOYER2);
    }

    public static Employer employer1WithDishes() {
        EMPLOYER1.setVacancies(Arrays.asList(VACANCY1));
        return EMPLOYER1;
    }

    public static Employer getNew() {
        return new Employer(null, "Созданный Employer", "NewYork", "https://grc.ua");
    }

    public static Employer getUpdated() {
        return new Employer(EMPLOYER1_ID, "Обновленный Employer", "updatedCity", "https://grc.ua");
    }

}
