package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
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
                "", "для этого фильтра нет вакансий", null, null, false ))
                : vacancies.stream()
                .map(vacancy -> createTo(vacancy, votes))
                .collect(Collectors.toList());
    }

    private static VacancyTo createTo(Vacancy v, List<Vote> votes) {
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

    public static Vacancy getVacancyFromTo(VacancyTo vTo){
        return new Vacancy(null, vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                thisDay, vTo.getLanguage(), vTo.getAddress(), LocalDateTime.now());
    }

    public static String getSiteName(String url) {
        Matcher matcher = Pattern.compile("h.+//.+?/").matcher(url);
        while (matcher.find()) {
            url = url.substring(matcher.start(), matcher.end());
        }
        return url;
    }

}
