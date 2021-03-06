package ua.training.top.util;

import ua.training.top.model.Employer;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

public class EmployerUtil {
    public static final String EMPLOYER_NOT_BE_NULL = "freshen must not be null";

    public static List<Employer> getEmployersFromTos(List<VacancyTo> vTos) {
        return vTos.stream().map(EmployerUtil::getEmployerFromTo).collect(Collectors.toList());
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress());
    }

    public static Map<String, Employer> getEmployerMap(Set<Employer> employers) {
        return employers.stream()
                .filter(employer -> hasText(employer.getAddress()) && hasText(employer.getName()))
                .collect(Collectors.toMap(employer -> employer.getName().concat(employer.getAddress()), employer -> employer));
    }

    public static void checkDataEmployer(Employer e){
        String[] data = {e.getName(), e.getAddress()};
        for(String text : data) {
            if (text == null || text.equals("")) {
                throw new IllegalArgumentException("must not null data of " + e.getClass().getSimpleName());
            }
        }
    }
}
