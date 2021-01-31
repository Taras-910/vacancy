package ua.training.top.util;

import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancySubTo;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VacancyUtil {

    public static List<VacancyTo> getTos(List<Vacancy> vacancies, List<Vote> votes) {
        return vacancies.isEmpty() ? getEmpty() : vacancies.stream().map(vacancy -> getTo(vacancy, votes)).collect(Collectors.toList());
    }

    private static List<VacancyTo> getEmpty() {
        return List.of(new VacancyTo(1000000, "not exists", "not exists", "not exists", 2, 2,"not exists",
                "there are no suitable vacancies in the database", LocalDate.now(), "not exists", "not exists", "not exists",false));
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

    public static Map<VacancyTo, Vacancy> getParallelMap(List<Vacancy> vacanciesDb, List<Vote> votes) {
        return vacanciesDb.stream().collect(Collectors.toMap(v -> getTo(v, votes), v -> v));
    }

    public static Vacancy populateVacancy(VacancyTo vacancy, VacancyTo vacancyDbTos, Map<VacancyTo, Vacancy> parallelMap) {
        Vacancy vacancyForUpdate = new Vacancy(fromTo(vacancy));
        vacancyForUpdate.setId(vacancyDbTos.getId());
        vacancyForUpdate.setEmployer(parallelMap.get(vacancyDbTos).getEmployer());
        vacancyForUpdate.setFreshen(parallelMap.get(vacancyDbTos).getFreshen());
        return vacancyForUpdate;
    }

    public static Map<VacancySubTo, VacancyTo> getMapVacancyTos(List<VacancyTo> vacancyTos) {
        return vacancyTos.stream().collect(Collectors.toMap(v ->
                new VacancySubTo(v.getId(),v.getTitle(), v.getEmployerName(), v.getSkills()), v -> v));
    }
}
