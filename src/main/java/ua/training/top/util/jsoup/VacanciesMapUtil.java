package ua.training.top.util.jsoup;

import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.util.*;

import static ua.training.top.util.VacancyUtil.fromTo;

public class VacanciesMapUtil {

    public static Map<Integer, List<Vacancy>> getMapVacanciesForUpdate(Set<Employer> employersForUpdate, Set<Vacancy> vacanciesForUpdate) {
        Map<Integer, List<Vacancy>> mapForUpdate = new HashMap<>();
        employersForUpdate.forEach(e -> {
            List<Vacancy> vacancies =  new ArrayList<>();
            vacanciesForUpdate.forEach(v -> {
                if(e.getId() == v.getEmployer().getId()){
                    vacancies.add(v);
                }
            });
            mapForUpdate.put(e.getId(), vacancies);
        });
        return mapForUpdate;
    }

    public static Map<Integer, List<Vacancy>> getMapVacanciesForCreate(List<Employer> employersCreated, Set<VacancyTo> tosExistdEmployers) {
        Map<Integer, List<Vacancy>> mapForCreate = new HashMap<>();
        employersCreated.forEach(e -> {
            List<Vacancy> vacancies =  new ArrayList<>();
            tosExistdEmployers.forEach(t -> {
                if(e.getName().equals(t.getEmployerName())){
                    vacancies.add(fromTo(t));
                }
            });
            mapForCreate.put(e.getId(), vacancies);
        });
        return mapForCreate;
    }

}
