package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vacancy;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.util.DateTimeUtil.toDate;

public class VacancyTestData {
    public static TestMatcher<Vacancy> VACANCY_MATCHER = TestMatcher.usingFieldsComparator("employer", "localDate");

    public static final int VACANCY1_ID = START_SEQ + 4;
    public static final int VACANCY2_ID = VACANCY1_ID + 1;
    public static final Vacancy VACANCY1 = new Vacancy(VACANCY1_ID,"Middle Game Developer", toDate(2020, 10, 25),
            0, "https://grc.ua/vacancy/40006938?query=java",
            "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…");
    public static final Vacancy VACANCY2 = new Vacancy(VACANCY2_ID, "Middle Java-разработчик", toDate(2020, 10, 25),
            0, "https://grc.ua/vacancy/40006938?query=java",
            "...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения");
    public static final Vacancy VACANCY3 = new Vacancy(VACANCY1_ID,"new Game Developer", toDate(2020, 10, 25), 1, "1", "1");
    public static final Vacancy VACANCY4 = new Vacancy(VACANCY1_ID,"new Developer", toDate(2020, 10, 25),2, "2", "2");

    public static final List<Vacancy> VACANCIES_GET_ALL = Arrays.asList(VACANCY1, VACANCY2);

    public static List<Vacancy> getListVacancies() {
        return Arrays.asList(VACANCY3, VACANCY4);
    }

    public static Vacancy getNew() {
        return new Vacancy("new Developer", toDate(2020, 10, 26), 500, "", "new knowledge");
    }

    public static Vacancy getUpdated() {
        return new Vacancy(VACANCY1_ID, "update Developer", new Date(2020, 10, 26), 700, "", "update knowledge");
    }
}
