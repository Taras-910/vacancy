package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VacancyUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyUtil.class) ;

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.isEmpty() ? getEmpty() : vacancies.stream().map(vacancy -> getTo(vacancy, votes)).collect(Collectors.toList());
    }

    public static VacancyTo getTo(Vacancy v, List<Vote> votes) {
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
        return new VacancyTo(v.getId(), v.getTitle(), v.getEmployer().getName(), v.getEmployer().getAddress(), v.getSalaryMin(),
                v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(), v.getEmployer().getSiteName(),
                v.getFreshen().getLanguage(), v.getFreshen().getWorkplace(), toVote);
    }

    public static List<Vacancy> fromTos (List<VacancyTo> vTos) {
        return vTos.stream().map(vTo -> fromTo(vTo)).collect(Collectors.toList());
    }

    public static Vacancy fromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(),
                vTo.getUrl(), vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    private static List<VacancyTo> getEmpty() {
        return List.of(new VacancyTo(0, "", "", "", -1, -1,"",
                "there are no suitable vacancies in the database", LocalDate.now(), "", "", "",false));
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        Vacancy vacancy = new Vacancy(v == null ? null : v.getId(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                v != null ? v.getReleaseDate() : vTo.getReleaseDate() != null ? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
        vacancy.setEmployer(v.getEmployer());
        vacancy.setFreshen(v.getFreshen());
        return vacancy;
    }

    public static boolean isNotSimilar(Vacancy v, VacancyTo vTo) {
        return !v.getTitle().equals(vTo.getTitle()) ||
                !v.getEmployer().getName().equals(vTo.getEmployerName()) ||
                !v.getSkills().equals(vTo.getSkills());
    }

    public static List<Vacancy> checkEmptyList(List<Vacancy> list, Freshen f) {
        if (list.isEmpty()) {
            log.error("database has not suitable vacancies for query: {"+ f.getLanguage() + ", "+ f.getWorkplace() + "}");
            return new ArrayList<>();
        }
        return list;
    }

    public static void isNullPointerException(VacancyTo vacancyTo) {
        if(!checkNullDataVacancyTo(vacancyTo)) {
            throw new NullPointerException("data not be null" + vacancyTo);
        }
    }

    public static boolean checkNullDataVacancyTo(VacancyTo v) {
        String[] line = {v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSkills(), v.getUrl()};
        for(String text : line) {
            if (text == null || text.equals("")) {
                log.error("Error null data of vacancy {}", v);
                return false;
            }
        }
        return true;
    }
}
