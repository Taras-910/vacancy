package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ua.training.top.util.DateTimeUtil.clearTime;
import static ua.training.top.util.DateTimeUtil.getTimestamp;

public class VacancyUtil {
    private static Logger log = LoggerFactory.getLogger(VacancyUtil.class);

    public static final String WORKPLACE_DEFAULT = "киев";
    public static final String LANGUAGE_DEFAULT = "java";

    public static ResponseEntity<String> getResult(BindingResult result) {
        String errorFieldsMsg = result.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
        return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
    }

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.size() == 0 ? getEmptyVacancyTos() : vacancies.stream().map(vacancy -> createTo(vacancy, votes)).collect(Collectors.toList());
    }

    public static VacancyTo createTo(Vacancy v, List<Vote> votes) {
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
//        log.info("\n\ntoVote {}\n", toVote);
        return new VacancyTo(v.getId(), v.getTitle(), v.getEmployer().getName(), v.getEmployer().getAddress(), v.getSalaryMin(),
                v.getSalaryMax(), v.getUrl(), v.getSkills(), clearTime(v.getReleaseDate()), v.getEmployer().getSiteName(), v.getLanguage(), v.getWorkplace(), toVote);
    }

    public static String getSiteName(String url) {
        Matcher matcher = Pattern.compile("h.+//.+?/").matcher(url);
        while (matcher.find()) {
            url = url.substring(matcher.start(), matcher.end());
        }
        return url;
    }

    public static DoubleString getLowerCase(DoubleString task) {
        String language = task.getLanguageTask() != null && !task.getLanguageTask().equals("") ? task.getLanguageTask().toLowerCase() : LANGUAGE_DEFAULT;
        String workplace = task.getWorkplaceTask() != null && !task.getWorkplaceTask().equals("") ? task.getWorkplaceTask().toLowerCase() : WORKPLACE_DEFAULT;
        return new DoubleString(language, workplace);
    }


    public static Vacancy getVacancyFromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                vTo.getReleaseDate() != null? vTo.getReleaseDate() : clearTime(new Date()), vTo.getLanguage().toLowerCase(), vTo.getAddress().toLowerCase(), LocalDateTime.now());
    }

    public static List<VacancyTo> getEmptyVacancyTos() {
        return List.of(new VacancyTo(null, "", "", "", 0, 0,
                "", "для этого фильтра нет вакансий", null, null, null, null,false));
    }


    public static List<VacancyTo> getTestList(){
        Employer EMPLOYER1 = new Employer(100002, "Huuuge Games", "Киев", "https://grc.ua");
        Employer EMPLOYER2 = new Employer(100003, "RedLab", "Киев", "https://grc.ua");

        Vacancy VACANCY1 = new Vacancy(100004,"Middle Game Developer",
                0, 1, "https://grc.ua/vacancy/40006938?query=java",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                getTimestamp(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
        Vacancy VACANCY2 = new Vacancy(100005, "Middle Java-разработчик",
                0, 1, "https://grc.ua/vacancy/40006938?query=java",
                "...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения",
                getTimestamp(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));

//  100004
        VacancyTo VACANCY_T05 = new VacancyTo(null,"Middle проверка vote Developer", "Huuuge Games", "",200000, 300000, "https проверка vote",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                getTimestamp(2020, 12, 25), "https проверка vote", "java", "киев", false);


//100004,Middle Game Developer,0,1,https://grc.ua/vacancy/40006938?query=java,Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…,2020-10-25 00:00:00.000000,java,киев,2020-11-01 12:00:00.000000,100002


        VacancyTo VACANCY_T01 = new VacancyTo(null,"Junior", "Huuuge Games", "Киев",300000, 450000, "https://grc.ua/vacancy/40006938?query=java",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                getTimestamp(2020, 10, 25), "https://grc.ua", "java", "киев", false);
// 100005

        VacancyTo VACANCY_T02 = new VacancyTo(null, "Java-разработчик", "New Company", "Киев",
                100000, 150000, "https://grc.ua/vacancy/40006938?query=java",
                "Java. Понимание ",
                getTimestamp(2020, 10, 25), "https://grc.ua", "java", "киев", false);



        return List.of(VACANCY_T05,VACANCY_T02, VACANCY_T01);
//        return List.of(VACANCY_T02);
    }
}
