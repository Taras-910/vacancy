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
    private static Logger log = LoggerFactory.getLogger(VacancyUtil.class);

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.size() == 0 ? getEmptyTos() : vacancies.stream().map(vacancy -> getTo(vacancy, votes)).collect(Collectors.toList());
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
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(),
                vTo.getUrl(), vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now());
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        return new Vacancy(v.getId(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                vTo.getReleaseDate() == null ? v.getReleaseDate() : vTo.getReleaseDate());
    }

    public static List<VacancyTo> getEmptyTos() {
        return List.of(new VacancyTo(null, "", "", "", null, null,"",
                "no suitable vacancies", null, null, null, null,false));
    }

    public static void checkUpdateVacancyTo(VacancyTo vacancyTo, Vacancy vacancyDb) {
        boolean check = vacancyTo.getSalaryMin().equals(vacancyDb.getSalaryMin())
                && vacancyTo.getSalaryMax().equals(vacancyDb.getSalaryMax())
                && vacancyTo.getUrl().equals(vacancyDb.getUrl())
                && vacancyTo.getSkills().equals(vacancyDb.getSkills());
        if (check) log.error("no data item has changed on " + vacancyTo);
    }
}
