package ua.training.top.dataTest;

import ua.training.top.TestMatcher;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ua.training.top.dataTest.EmployerTestData.employer1;
import static ua.training.top.dataTest.EmployerTestData.employer2;
import static ua.training.top.model.AbstractBaseEntity.START_SEQ;

public class VacancyTestData {
    public static TestMatcher<Vacancy> VACANCY_MATCHER = TestMatcher.usingFieldsComparator(Vacancy.class,"employer", "recordedDate");

    public static final int VACANCY1_ID = START_SEQ + 4;
    public static final int VACANCY2_ID = VACANCY1_ID + 1;
    public static final Vacancy vacancy1 = new Vacancy(VACANCY1_ID,"Middle Game Developer",
            0, 1, "https://grc.ua/vacancy/40006938?query=java",
            "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
            LocalDate.of(2020, 10, 25),"php", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
    public static final Vacancy vacancy2 = new Vacancy(VACANCY2_ID, "Middle Java-разработчик",
            0, 1, "https://grc.ua/vacancy/40006938?query=java",
            "...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения",
            LocalDate.of(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));

    public static final Vacancy vacancy3 = new Vacancy(null,"new Game Developer", 50,100, "https://www.w3schools.com/1/1.asp", "newSkills1",
            LocalDate.now().minusDays(5), "java", "киев", LocalDateTime.now());
    public static final Vacancy vacancy4 = new Vacancy(null,"new Developer", 100, 200, "https://www.w3schools.com/2/2.asp", "newSkills2",
            LocalDate.now().minusDays(5),"java", "киев", LocalDateTime.now());

    public static final List<Vacancy> VACANCIES_GET_ALL = getAllWithEmployers();

    private static List<Vacancy> getAllWithEmployers() {
        vacancy1.setEmployer(employer1);
        vacancy2.setEmployer(employer2);
        return  Arrays.asList(vacancy2, vacancy1);
    }

    public static List<Vacancy> getListVacancies() {
        return Arrays.asList(vacancy3, vacancy4);
    }

    public static VacancyTo getNew() {
        return new VacancyTo(null, "Junior", "newEmployment", "киев, метро Вокзальная", 100, 500, "https://www.baeldung.com/spring-type-conversions", "new knowledge",
                LocalDate.of(2020, 10, 26), "https://www.baeldung.com", "java", "киев", false);
    }

    public static VacancyTo getUpdated() {
        return new VacancyTo(VACANCY1_ID, "Middle Game Developer", "updatedEmployer", "киев, метро Вокзальная",200,700, "", "update knowledge",
                LocalDate.of(2020, 10, 26), "", "java", "киев", false);
    }
//    public VacancyTo(Integer id, @NotNull String title, @NotNull String employerName, @NotNull String address, Integer salaryMin,
//                     Integer salaryMax, String url, String skills, @NotNull Date releaseDate, String language, boolean toVote) {
}
