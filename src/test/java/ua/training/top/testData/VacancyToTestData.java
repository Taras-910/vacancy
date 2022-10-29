package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;

import static ua.training.top.testData.VacancyTestData.VACANCY1_ID;

public class VacancyToTestData {
    public static final TestMatcher<VacancyTo> VACANCY_TO_MATCHER = TestMatcher.usingFieldsComparator(VacancyTo.class, "");

    public static final VacancyTo vacancyTo1 = new VacancyTo(VACANCY1_ID, "Middle Game Developer",
            "Huuuge Games", "Киев", 100000, 200000,
            "https://grc.ua/vacancy/40006938?query=java",
            "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
            LocalDate.of(2020, 10, 25), "java", "middle", "киев", true);

    public static VacancyTo getNew() {
        return new VacancyTo(null, "NewJavaMiddle", "NewMicrosoft", "Киев", 1, 1,
                "https://www.w3schools.com/jquery/jquery_filters.asp", "Java Core", LocalDate.now(),
                "java", "middle", "киев", false);
    }

    public static VacancyTo getUpdate() {
        VacancyTo vTo = new VacancyTo(vacancyTo1);
        vTo.setUrl("newURL");
        vTo.setSalaryMin(10000);
        vTo.setSalaryMax(20000);
        vTo.setSkills("newSkills");
        return vTo;
    }
}
