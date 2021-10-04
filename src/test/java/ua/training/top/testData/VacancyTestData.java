package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.testData.EmployerTestData.employer1;
import static ua.training.top.testData.EmployerTestData.employer2;
import static ua.training.top.testData.FreshenTestData.freshen1;
import static ua.training.top.testData.FreshenTestData.freshen2;

public class VacancyTestData {
    public static TestMatcher<Vacancy> VACANCY_MATCHER = TestMatcher
            .usingFieldsComparator(Vacancy.class,"employer", "freshen", "recordedDate");

    public static final int VACANCY1_ID = START_SEQ + 6;
    public static final int VACANCY2_ID = VACANCY1_ID + 1;
    public static final Vacancy vacancy1 = getVacancy1();
    public static final Vacancy vacancy2 = getVacancy2();

    private static Vacancy getVacancy2() {
        Vacancy vacancy = new Vacancy(VACANCY2_ID, "Middle Java-разработчик",
                150000, 300000, "https://grc.ua/vacancy/40006938?query=java",
                "...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения",
                LocalDate.of(2020, 10, 25));
        vacancy.setFreshen(freshen2);
        vacancy.setEmployer(employer2);
        return vacancy;
    }
    private static Vacancy getVacancy1() {
        Vacancy vacancy =new Vacancy(VACANCY1_ID,"Middle Game Developer",
                100000, 200000, "https://grc.ua/vacancy/40006938?query=java",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                LocalDate.of(2020, 10, 25));
        vacancy.setFreshen(freshen1);
        vacancy.setEmployer(employer1);
        return vacancy;
    }
    public static final Vacancy vacancy3 = new Vacancy(null,"new Game Developer", 50,100,
            "https://www.w3schools.com/1/1.asp", "newSkills1",
            LocalDate.now().minusDays(5));
    public static final Vacancy vacancy4 = new Vacancy(null,"new Developer", 100, 200,
            "https://www.w3schools.com/2/2.asp", "newSkills2",
            LocalDate.now().minusDays(5));

    public static final List<Vacancy> VACANCIES_GET_ALL = getAllWithEmployers();

    private static List<Vacancy> getAllWithEmployers() {
        vacancy1.setEmployer(employer1);
        vacancy2.setEmployer(employer2);
        return  Arrays.asList(vacancy1, vacancy2);
    }

    public static List<Vacancy> getListVacancies() {
        return Arrays.asList(vacancy3, vacancy4);
    }

    public static VacancyTo getNew() {
        return new VacancyTo(null, "Junior", "newEmployment", "киев, метро Вокзальная",
                100, 500,
                "https://www.baeldung.com/spring-type-conversions", "new knowledge",
                LocalDate.of(2020, 10, 26), "java", "junior",
                "киев", false);
    }

    public static VacancyTo getToUpdated() {
        return new VacancyTo(VACANCY1_ID, "Middle Game Developer", "Huuuge Games", "Киев",
                22222,77777, "https://www.updated.com?language=spring",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                LocalDate.of(2020, 10, 25), "java", "middle", "киев", false);
    }
}
