package ua.training.top.aggregator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Employer;
import ua.training.top.to.VacancyNet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployerNetUtil {
    private static Logger log = LoggerFactory.getLogger(EmployerNetUtil.class);

    public static List<Employer> getEmployers(List<VacancyNet> vacancyNets) {
        Set<Employer> set = new LinkedHashSet<>();
        set = vacancyNets.stream()
                .map(vacancy -> createEmployer(vacancy))
                .collect(Collectors.toSet());
        return new ArrayList<>(set);
    }

    private static Employer createEmployer(VacancyNet v) {
        return new Employer(null, v.getCompanyName(), v.getCity(), v.getSiteName());
    }
}
