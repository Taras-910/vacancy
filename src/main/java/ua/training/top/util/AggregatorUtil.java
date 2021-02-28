package ua.training.top.util;

import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.SubVacancyTo;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.fromTo;
import static ua.training.top.util.VacancyUtil.getTo;

public class AggregatorUtil {

    public static VacancyTo getFilled(VacancyTo vacancyTo, Freshen freshen) {
        vacancyTo.setWorkplace(freshen.getWorkplace());
        vacancyTo.setLanguage(freshen.getLanguage());
        vacancyTo.setToVote(false);
        return vacancyTo;
    }

    public static List<Vacancy> getForCreate(List<VacancyTo> vacancyTosForCreate, Map<String, Employer> mapAllEmployers, Freshen freshenDb) {
        return vacancyTosForCreate.stream()
                .map(vTo -> {
                    Vacancy vacancy = fromTo(vTo);
                    vacancy.setEmployer(mapAllEmployers.get(vTo.getEmployerName().concat(vTo.getAddress())));
                    vacancy.setFreshen(freshenDb);
                    return vacancy;
                })
                .collect(Collectors.toList());
    }

    public static Vacancy getForUpdate(VacancyTo vacancy, VacancyTo vacancyDbTos, Map<VacancyTo, Vacancy> parallelMap) {
        Vacancy vacancyForUpdate = new Vacancy(fromTo(vacancy));
        vacancyForUpdate.setId(vacancyDbTos.getId());
        vacancyForUpdate.setEmployer(parallelMap.get(vacancyDbTos).getEmployer());
        vacancyForUpdate.setFreshen(parallelMap.get(vacancyDbTos).getFreshen());
        return vacancyForUpdate;
    }

    public static Map<SubVacancyTo, VacancyTo> getMapVacancyTos(List<VacancyTo> vacancyTos) {
        return vacancyTos.stream().collect(Collectors.toMap(v ->
                new SubVacancyTo(v.getTitle(), v.getEmployerName(), v.getSkills()), v -> v));
    }

    public static Map<VacancyTo, Vacancy> getParallelMap(List<Vacancy> vacanciesDb, List<Vote> votes) {
        return vacanciesDb.stream().collect(Collectors.toMap(v -> getTo(v, votes), v -> v));
    }
}
