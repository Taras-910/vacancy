package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.EmployerTestData.EMPLOYER1;
import static ua.training.top.testData.EmployerTestData.EMPLOYER2;
import static ua.training.top.util.DateTimeUtil.getTimestamp;
import static ua.training.top.util.DateTimeUtil.toDate;

public class VacancyTestData {
    public static TestMatcher<Vacancy> VACANCY_MATCHER = TestMatcher.usingFieldsComparator("employer");

    public static final int VACANCY1_ID = START_SEQ + 4;
    public static final int VACANCY2_ID = VACANCY1_ID + 1;
    public static final Vacancy VACANCY1 = new Vacancy(VACANCY1_ID,"Middle Game Developer",
            0, 1, "https://grc.ua/vacancy/40006938?query=java",
            "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
            getTimestamp(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
    public static final Vacancy VACANCY2 = new Vacancy(VACANCY2_ID, "Middle Java-разработчик",
            0, 1, "https://grc.ua/vacancy/40006938?query=java",
            "...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения",
            getTimestamp(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
    public static final Vacancy VACANCY3 = new Vacancy(null,"new Game Developer", 50,100, "1", "1",
            getTimestamp(2020, 10, 25), "java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
    public static final Vacancy VACANCY4 = new Vacancy(null,"new Developer", 100, 200, "2", "2",
            getTimestamp(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));

    public static final List<Vacancy> VACANCIES_GET_ALL = getAllWithEmployers();

    private static List<Vacancy> getAllWithEmployers() {
        VACANCY1.setEmployer(EMPLOYER1);
        VACANCY2.setEmployer(EMPLOYER2);
        return  Arrays.asList(VACANCY2, VACANCY1);
    }

    public static List<Vacancy> getListVacancies() {
        return Arrays.asList(VACANCY3, VACANCY4);
    }

    public static Vacancy getNew() {
        return new Vacancy("new Developer", 100, 500, "", "new knowledge",
                toDate(2020, 10, 26), "java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
    }

    public static Vacancy getUpdated() {
        return new Vacancy(VACANCY1_ID, "update Developer", 200,700, "", "update knowledge",
                new Date(2020, 10, 26), "java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
    }
}
