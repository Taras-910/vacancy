package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.util.xss.SafeFromXssUtil.getXssCleaned;

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
                v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(), v.getEmployer().getSiteName(), v.getLanguage(), v.getWorkplace(), toVote);
    }

    public static String getSiteName(String url) {
        Matcher matcher = Pattern.compile("h.+//.+?/").matcher(url);
        while (matcher.find()) {
            url = url.substring(matcher.start(), matcher.end());
        }
        return url;
    }

    public static Vacancy getVacancyFromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(),
                vTo.getUrl(), vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now(),
                vTo.getLanguage(), nullOrEmpty(vTo.getWorkplace()) ? vTo.getWorkplace() : vTo.getAddress().toLowerCase(), LocalDateTime.now());
    }

    public static Vacancy getVacancyForUpdate(VacancyTo vTo, Vacancy v) {
        return new Vacancy(v.getId(), v.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                vTo.getReleaseDate() == null ? v.getReleaseDate() : vTo.getReleaseDate(), vTo.getLanguage(),
                vTo.getWorkplace() == null ? v.getWorkplace() : vTo.getAddress().toLowerCase(), v.getRecordedDate());
    }

    public static boolean nullOrEmpty(String obj){
        return obj == null || obj.isEmpty();
    }
    public static List<VacancyTo> getEmptyVacancyTos() {
        return List.of(new VacancyTo(null, "", "", "", 0, 0,
                "", "для этого фильтра нет вакансий", null, null, null, null,false));
    }
//    public Freshen(Integer id, LocalDateTime freshenDateTime, String language, String workplace, Integer userId) {
    public static Freshen getValidFreshen(Freshen f) {
        return new Freshen(null, LocalDateTime.now(), getXssCleaned(f.getLanguage()).toLowerCase(), getXssCleaned(f.getWorkplace()).toLowerCase(), authUserId());
    }

    public static List<VacancyTo> getTestList(){
        Employer EMPLOYER1 = new Employer(100002, "Huuuge Games", "Киев", "https://grc.ua");
        Employer EMPLOYER2 = new Employer(100003, "RedLab", "Киев", "https://grc.ua");

        Vacancy VACANCY1 = new Vacancy(100004,"Middle Game Developer",
                0, 1, "https://grc.ua/vacancy/40006938?query=java",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                LocalDate.of(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
        Vacancy VACANCY2 = new Vacancy(100005, "Middle Java-разработчик",
                0, 1, "https://grc.ua/vacancy/40006938?query=java",
                "...на Java от 2-х лет. Понимание устройства и основных принципов работы платформы JVM. Умение отлаживать и профилировать java-приложения",
                LocalDate.of(2020, 10, 25),"java", "киев", LocalDateTime.of(2020, 11, 1, 12, 00));
//  100004
        VacancyTo VACANCY_T05 = new VacancyTo(null,"Middle проверка vote Developer", "New Company Games", "киев",200000, 300000, "https проверка vote",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting java…",
                LocalDate.now().minusDays(7), "https://ukr.net/проверка/vote", null, null, false);

        VacancyTo VACANCY_T01 = new VacancyTo(null,"Junior java", "Games", "Poznan",300000, 450000, "https://grc.ua/vacancy/40006938?query=java",
                "Still haven’t found your dream job? Huuuge Games is a gaming company on a mission to build the world’s largest real-time casual gaming platform connecting…",
                LocalDate.now().minusDays(5), "https://grc.ua", null, null, false);
// 100005
        VacancyTo VACANCY_T02 = new VacancyTo(null, "Java-разработчик", "New Company", "Warshaw",
                100000, 150000, "https://grc.ua/vacancy/40006938?query=java",
                "Java. Понимание javaScript",
                LocalDate.now().minusDays(3), "https://grc.ua", null, null, false);
        return List.of(VACANCY_T05, VACANCY_T02, VACANCY_T01);
//        return List.of(VACANCY_T05);
    }
}
