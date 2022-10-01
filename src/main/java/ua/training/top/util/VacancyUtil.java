package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.MessageUtil.*;
import static ua.training.top.util.collect.data.ConstantsUtil.link;

public class VacancyUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyUtil.class) ;

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.isEmpty() ? getEmpty() : vacancies.stream()
                        .map(vacancy -> getTo(vacancy, votes))
                        .collect(Collectors.toList());
    }

    public static VacancyTo getTo(Vacancy v, List<Vote> votes) {
        boolean toVote = votes.stream().anyMatch(vote -> v.getId().equals(vote.getVacancyId()));
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
                empty_inform, LocalDate.now(), "", "", "",false));
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        Vacancy vacancy = new Vacancy(
                v.getId(),
                vTo.getTitle().equals(link) ? v.getTitle() : vTo.getTitle(),
                vTo.getSalaryMin(),
                vTo.getSalaryMax(),
                vTo.getUrl(),
                vTo.getSkills().equals(link) ? v.getSkills() : vTo.getSkills(),
                vTo.getReleaseDate() != null ? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
        vacancy.setEmployer(v.getEmployer());
        vacancy.setFreshen(v.getFreshen());
        return vacancy;
    }

    public static boolean isNotSimilar(Vacancy v, VacancyTo vTo) {
        return !v.getTitle().equals(vTo.getTitle()) ||
                !v.getEmployer().getName().equals(vTo.getEmployerName()) ||
                !v.getSkills().equals(vTo.getSkills());
    }

    public static void isNullPointerException(VacancyTo vacancyTo) {
        if(!checkNullDataVacancyTo(vacancyTo)) {
            throw new NullPointerException(not_be_null + vacancyTo);
        }
    }

    public static boolean checkNullDataVacancyTo(VacancyTo v) {
        String[] line = {v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSkills(), v.getUrl()};
        for(String text : line) {
            if (text == null || text.equals("")) {
                log.error(check_error_data, v);
                return false;
            }
        }
        return true;
    }
}
