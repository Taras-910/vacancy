package ua.training.top.testData;

import ua.training.top.TestMatcher;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;

import static ua.training.top.testData.VacancyTestData.VACANCY1_ID;

public class VacancyToTestData {
    public static TestMatcher<VacancyTo> VACANCY_TO_MATCHER = TestMatcher.usingFieldsComparator(VacancyTo.class,"");

    public static final VacancyTo vacancyTo1 = new VacancyTo(VACANCY1_ID, "Middle Game Developer", "Huuuge Games", "Киев", 0, 1, "https://grc.ua/vacancy/40006938?query=java",
            "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
            LocalDate.of(2020, 10, 25), "https://grc.ua","php", "киев", true);


    public static VacancyTo getNew() {
        return new VacancyTo(null, "JavaMiddle", "Microsoft", "Киев", 1, 1, "https://www.w3schools.com/jquery/jquery_filters.asp", "Java Core", LocalDate.now(), "https://www.w3schools.com/", "java", "киев", false);
    }

    public static VacancyTo getUpdate() {
        VacancyTo vTo = new VacancyTo(vacancyTo1);
        vTo.setUrl("newURL");
        vTo.setSalaryMin(10000);
        vTo.setSalaryMax(20000);
        vTo.setSkills("newSkills");
        return vTo;
    }


//    public Vacancy(Integer id, String title,                                      Integer salaryMin, Integer salaryMax, String url,
//    String skills, LocalDate releaseDate,                  String language, String workplace, LocalDateTime recordedDate               )

//  public VacancyTo(Integer id, String title, String employerName, String address, Integer salaryMin, Integer salaryMax, String url,
//    String skills, LocalDate releaseDate, String siteName, String language, String workPlace,                            boolean toVote)

//    public static final Employer employer1 = new Employer(EMPLOYER1_ID, "Huuuge Games", "Киев", "https://grc.ua");

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
