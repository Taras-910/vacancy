package ua.training.top.aggregator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyNet;
import ua.training.top.util.DateTimeUtil;

import java.util.*;
import java.util.stream.Collectors;

public class VacancyNetUtil {
    private static Logger log = LoggerFactory.getLogger(VacancyNetUtil.class);

    public static List<Vacancy> getVacancies(List<VacancyNet> vacancyNets) {
        List<Vacancy> list = vacancyNets.stream()
                .map(vacancy -> createVacancy(vacancy))
                .distinct()
                .collect(Collectors.toList());
        Set<Vacancy> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    private static Vacancy createVacancy(VacancyNet v) {
        return new Vacancy(null, v.getTitle(), DateTimeUtil.parse(v.getDate(), null),
                salaryMin(v.getSalary()), salaryMax(v.getSalary()), v.getUrl(), v.getSkills());
    }

    private static Integer salaryMin(String salary) {
        salary = salary.equals("") ? "1" : salary;
        return salary.contains("-") ?  Integer.parseInt(salary.split("-")[0]) : Integer.parseInt(salary);
    }

    private static Integer salaryMax(String salary) {
        salary = salary.equals("") ? "1" : salary;
        return salary.contains("-") ?  Integer.parseInt(salary.split("-")[1]) : 1;
    }

    public static Map<Integer, List<Vacancy>> getMapVacancies(List<Employer> employersDb, List<VacancyNet> vacanciesNet) {
        Map<Integer, List<Vacancy>> map = new HashMap<>();
        for(Employer empl : employersDb) {
            List<VacancyNet> listNet = vacanciesNet.stream()
                    .filter(v -> empl.getName().equals(v.getCompanyName()))
                    .distinct()
                    .collect(Collectors.toList());
            List<Vacancy> vacancies  = getVacancies(listNet);
            map.put(empl.getId(), vacancies);
        }
        return map;
    }
}
