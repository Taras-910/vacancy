package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesToKeep;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodToKeep;

public class VacancyUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyUtil.class) ;

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.isEmpty() ? getEmpty() :
                vacancies.stream()
                        .map(vacancy -> getTo(vacancy, votes))
                        .sorted(VacancyTo::compareTo)
                        .collect(Collectors.toList());
    }

    public static VacancyTo getTo(Vacancy v, List<Vote> votes) {
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
        return new VacancyTo(v.getId(), v.getTitle(), v.getEmployer().getName(), v.getEmployer().getAddress(),
                v.getSalaryMin(), v.getSalaryMax(), v.getUrl(), v.getSkills(), v.getReleaseDate(),
                v.getFreshen().getLanguage(), v.getFreshen().getLevel(), v.getFreshen().getWorkplace(), toVote);
    }

    public static List<Vacancy> fromTos (List<VacancyTo> vTos) {
        return vTos.stream().map(VacancyUtil::fromTo).collect(Collectors.toList());
    }

    public static Vacancy fromTo(VacancyTo vTo) {
        return new Vacancy(vTo.getId(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(),
                vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    private static List<VacancyTo> getEmpty() {
        return of(new VacancyTo(0, "", "", "", -1, -1,"",
                "No filtering records found, refresh DB", LocalDate.now(), "", "",
                "",false));
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        Vacancy vacancy = new Vacancy(v == null ? null : v.getId(), vTo.getTitle(), vTo.getSalaryMin(),
                vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(), v != null ? v.getReleaseDate() :
                vTo.getReleaseDate() != null ? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
        assert v != null;
        vacancy.setEmployer(v.getEmployer());
        vacancy.setFreshen(v.getFreshen());
        return vacancy;
    }

    public static List<Vacancy> getVacanciesOutPeriodToKeep(List<Vacancy> vacanciesDb) {
        return vacanciesDb.parallelStream()
                .filter(vacancyTo -> reasonPeriodToKeep.isAfter(vacancyTo.getReleaseDate()))
                .collect(Collectors.toList());
    }

    public static List<Vacancy> getVacanciesOutLimitHeroku(List<Vacancy> vacanciesDb) {
        return Optional.of(vacanciesDb.parallelStream()
                .sorted((v1, v2) -> v1.getReleaseDate().isAfter(v2.getReleaseDate()) ? 1 : 0)
                .sequential()
                .skip(limitVacanciesToKeep)
                .collect(Collectors.toList())).orElse(new ArrayList<>());
    }

}
