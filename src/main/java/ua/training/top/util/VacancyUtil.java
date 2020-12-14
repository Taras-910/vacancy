package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.DoubleWordTo;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ua.training.top.util.DateTimeUtil.clearTime;
import static ua.training.top.util.DateTimeUtil.thisDay;

public class VacancyUtil {
    private static Logger log = LoggerFactory.getLogger(VacancyUtil.class);

    public static final String WORKPLACE_DEFAULT = "киев";
    public static final String LANGUAGE_DEFAULT = "java";

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.size() == 0 ? List.of(new VacancyTo(null, "", "", "", 0, 0,
                "", "для этого фильтра нет вакансий", null, null, false))
                : vacancies.stream()
                .map(vacancy -> createTo(vacancy, votes))
                .collect(Collectors.toList());
    }

    public static VacancyTo createTo(Vacancy v, List<Vote> votes) {
//        log.info("--------------------------------------------------------------------");
//        log.info("votes {}", votes);
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
//        log.info("\n\ntoVote {}\n", toVote);
        return new VacancyTo(v.getId(), v.getTitle(), v.getEmployer().getName(), v.getEmployer().getAddress(),
                v.getSalaryMin(), v.getSalaryMax(), v.getUrl(), v.getSkills(), clearTime(v.getReleaseDate()), v.getLanguage(), toVote);
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress(), getSiteName(vTo.getUrl()));
    }

    public static Vacancy getVacancyFromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                thisDay, vTo.getLanguage().toLowerCase(), vTo.getAddress().toLowerCase(), LocalDateTime.now());
    }

    public static String getSiteName(String url) {
        Matcher matcher = Pattern.compile("h.+//.+?/").matcher(url);
        while (matcher.find()) {
            url = url.substring(matcher.start(), matcher.end());
        }
        return url;
    }

    public static ResponseEntity<String> getResult(BindingResult result) {
        String errorFieldsMsg = result.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s] %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining("<br>"));
        return ResponseEntity.unprocessableEntity().body(errorFieldsMsg);
    }

    public static DoubleWordTo getLowerCase(DoubleWordTo task) {
        String language = task.getLanguageTask() != null && !task.getLanguageTask().equals("") ? task.getLanguageTask().toLowerCase() : LANGUAGE_DEFAULT;
        String workplace = task.getWorkplaceTask() != null && !task.getWorkplaceTask().equals("") ? task.getWorkplaceTask().toLowerCase() : WORKPLACE_DEFAULT;
        return new DoubleWordTo(language, workplace);
    }

    public static List<VacancyTo> getEmptyVacancyTos() {
        return List.of(new VacancyTo(null, "", "", "", null, null, "",
                "по этому запросу данных нет", null, "", false));
    }
}
