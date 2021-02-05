package ua.training.top.util;

import org.springframework.util.StringUtils;
import ua.training.top.model.Employer;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.util.parser.datas.CorrectSiteName.getSiteName;

public class EmployerUtil {
    public static List<Employer> getEmployersFromTos(List<VacancyTo> vTos) {
        return vTos.stream().map(EmployerUtil::getEmployerFromTo).collect(Collectors.toList());
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress(),
                StringUtils.hasText(vTo.getSiteName()) ? vTo.getSiteName() : getSiteName(vTo.getUrl()));
    }

    public static Map<String, Employer> getEmployerMap(Set<Employer> employers) {
        return employers.stream()
                .filter(employer -> hasText(employer.getAddress()) && hasText(employer.getName()))
                .collect(Collectors.toMap(Employer::getName, employer -> employer));
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
