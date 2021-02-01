package ua.training.top.util;

import org.springframework.util.StringUtils;
import ua.training.top.model.Employer;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.util.parser.datas.CorrectSiteName.getSiteName;

public class EmployerUtil {
    public static List<Employer> getEmployersFromTos(List<VacancyTo> vTos) {
        return vTos.stream().map(vTo -> getEmployerFromTo(vTo)).collect(Collectors.toList());
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        Employer employer = new Employer(null, vTo.getEmployerName(), vTo.getAddress(),
                StringUtils.hasText(vTo.getSiteName()) ? vTo.getSiteName() : getSiteName(vTo.getUrl()));
        Vacancy vacancy = VacancyUtil.fromTo(vTo);
        employer.setVacancies(List.of(vacancy));
        return employer;
    }
}
