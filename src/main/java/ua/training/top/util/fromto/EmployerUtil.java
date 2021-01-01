package ua.training.top.util.fromto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Employer;
import ua.training.top.to.VacancyTo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ua.training.top.util.VacancyUtil.getSiteName;

public class EmployerUtil {
    private static Logger log = LoggerFactory.getLogger(EmployerUtil.class);

    public static List<Employer> getEmployersFromTo(List<VacancyTo> vacancyTos) {
        Set<Employer> set = new LinkedHashSet<>();
        set = vacancyTos.stream()
                .map(vacancy -> getEmployerFromTo(vacancy))
                .collect(Collectors.toSet());
        return new ArrayList<>(set);
    }

    public static Employer getEmployerFromTo(VacancyTo vTo) {
        return new Employer(null, vTo.getEmployerName(), vTo.getAddress(),
                vTo.getSiteName() == null ? getSiteName(vTo.getUrl()) : vTo.getSiteName());
    }
}
