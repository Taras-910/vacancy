package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VacancyUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyUtil.class) ;

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.isEmpty() ? getEmpty() :
                vacancies.stream().map(vacancy -> getTo(vacancy, votes)).collect(Collectors.toList());
    }

    public static VacancyTo getTo(Vacancy v, List<Vote> votes) {
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
        return new VacancyTo(v.getId(), v.getTitle(), v.getEmployer().getName(), v.getEmployer().getAddress(),
                v.getSalaryMin(), v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(),
                v.getEmployer().getSiteName(), v.getFreshen().getLanguage(), v.getFreshen().getWorkplace(), toVote);
    }

    public static List<Vacancy> fromTos (List<VacancyTo> vTos) {
        return vTos.stream().map(vTo -> fromTo(vTo)).collect(Collectors.toList());
    }

    public static Vacancy fromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(),
                vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    private static List<VacancyTo> getEmpty() {
        return List.of(new VacancyTo(0, "", "", "", -1, -1,"",
                "в базе данных нет вакансий по этому запросу, обновите базу данных", LocalDate.now(), "", "",
                "",false));
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        Vacancy vacancy = new Vacancy(v == null ? null : v.getId(), vTo.getTitle(), vTo.getSalaryMin(),
                vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(), v != null ? v.getReleaseDate() :
                vTo.getReleaseDate() != null ? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
        vacancy.setEmployer(v.getEmployer());
        vacancy.setFreshen(v.getFreshen());
        return vacancy;
    }
}
