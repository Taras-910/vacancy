package ua.training.top.util;

import org.springframework.util.StringUtils;
import ua.training.top.model.Employer;
import ua.training.top.to.VacancyTo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.training.top.util.parser.datas.CorrectSiteName.getSiteName;

public class EmployerUtil {
    public static List<Employer> getEmployersFromTos(List<VacancyTo> vTos) {
        return vTos.stream().map(vTo -> getEmployerFromTo(vTo)).collect(Collectors.toList());
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress(),
                StringUtils.hasText(vTo.getSiteName()) ? vTo.getSiteName() : getSiteName(vTo.getUrl()));
    }


    public static List<Employer> getEmployersSuitable(List<VacancyTo> vacancyTos, List<Employer> employersAll) {
        List<Employer> suitable = new ArrayList<>();
        List<Employer> fromTos = getEmployersFromTos(vacancyTos);
        for(Employer e : employersAll) {
            if(fromTos.contains(e)) {
                suitable.add(e);
            }
        }
        return suitable;
    }

    public static List<Employer> getEmployersForCreate(List<VacancyTo> vacancyTos, List<Employer> employersDb) {
        Set<Employer> employersForCreate = new HashSet<>();
        for(VacancyTo vTo : vacancyTos ){
            Employer employer = getEmployerFromTo(vTo);
            if (!employersDb.contains(employer)){
                employersForCreate.add(employer);
            }
        }
        return new ArrayList<>(employersForCreate);
    }


}
