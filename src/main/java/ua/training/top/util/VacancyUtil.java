package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.DateTimeUtil.clearTime;

public class VacancyUtil {
    private static Logger log = LoggerFactory.getLogger(VacancyUtil.class);

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.stream()
                .map(vacancy -> createTo(vacancy, votes))
                .collect(Collectors.toList());
    }

    private static VacancyTo createTo(Vacancy v, List<Vote> votes) {
        log.info("--------------------------------------------------------------------");
        log.info("votes {}", votes);
        boolean toVote = votes.stream().filter(vote -> v.getId().equals(vote.getVacancyId())).count() != 0;
        log.info("\n\ntoVote {}\n", toVote);
        return new VacancyTo(v.getId(), v.getEmployer().getName(), v.getEmployer().getAddress(), v.getName(),
                clearTime(v.getLocalDate()), v.getSalary(), v.getLink(), v.getSkills(), toVote);
    }
}
