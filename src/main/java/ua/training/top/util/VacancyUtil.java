package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.model.Vote;
import ua.training.top.to.VacancyTo;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class VacancyUtil {
    private static Logger log = LoggerFactory.getLogger(VacancyUtil.class);

    public static Map<Integer, List<Vacancy>> getMapVacanciesForCreate(List<Employer> employersCreated, Set<VacancyTo> tosExistdEmployers) {
        Map<Integer, List<Vacancy>> mapForCreate = new HashMap<>();
        employersCreated.forEach(employer -> {
            List<Vacancy> vacancies =  new ArrayList<>();
            tosExistdEmployers.forEach(vacancyTo -> {
                if(employer.getName().equals(vacancyTo.getEmployerName())){
                    vacancies.add(fromTo(vacancyTo));
                }
            });
            mapForCreate.put(employer.getId(), vacancies);
        });
        return mapForCreate;
    }

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
        return new Vacancy(vTo.getId() == null ? null : vTo.id(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(),
                vTo.getUrl(), vTo.getSkills(), vTo.getReleaseDate() != null? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    public static Vacancy getForUpdate(VacancyTo vTo, Vacancy v) {
        return new Vacancy(v == null ? null : v.getId(), vTo.getTitle(), vTo.getSalaryMin(), vTo.getSalaryMax(), vTo.getUrl(), vTo.getSkills(),
                v != null ? v.getReleaseDate() : vTo.getReleaseDate() != null ? vTo.getReleaseDate() : LocalDate.now().minusDays(7));
    }

    public static boolean vacancyNotSame(VacancyTo vTo, Vacancy vFind) {
        return !vFind.getTitle().equals(vTo.getTitle()) ||
                vFind.getSalaryMax() != vTo.getSalaryMax() ||
                vFind.getSalaryMin() != vTo.getSalaryMin() ||
                !vFind.getSkills().equals(vTo.getSkills());
    }

    public static boolean checkNotSimilarVacancy(Vacancy v, VacancyTo vTo) {
        return !v.getTitle().equals(vTo.getTitle()) ||
                !v.getEmployer().getName().equals(vTo.getEmployerName()) ||
                !v.getSkills().equals(vTo.getSkills());
    }

    public static boolean testNotSimilar(List<Vacancy> vacanciesDb, VacancyTo vTo) {
        AtomicBoolean test = new AtomicBoolean(false);
        vacanciesDb.forEach(v -> {
            if(checkNotSimilarVacancy(v, vTo)){
                test.set(true);
            }
        });
        return test.get();
    }
}
