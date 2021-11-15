package ua.training.top.util;

import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import static java.lang.String.join;
import static ua.training.top.aggregator.installation.InstallationUtil.maxLengthText;
import static ua.training.top.util.parser.data.DataUtil.*;

public class AggregatorUtil {

    public static VacancyTo getFilled(VacancyTo vacancyTo, Freshen freshen) {
        vacancyTo.setWorkplace(freshen.getWorkplace());
        vacancyTo.setLevel(freshen.getLevel());
        vacancyTo.setLanguage(freshen.getLanguage());
        vacancyTo.setToVote(false);
        return vacancyTo;
    }

    public static String getAnchorVacancy(Vacancy v) {
        return getByLimit(join(v.getTitle().replaceAll(" ", ""), v.getEmployer().getName(),
                v.getEmployer().getAddress(), v.getSkills()), maxLengthText / 2).toLowerCase();
    }

    public static String getAnchorVacancy(VacancyTo vTo) {
        return getByLimit(join(vTo.getTitle().replaceAll(" ", ""), vTo.getEmployerName(),
                vTo.getAddress(), vTo.getSkills()), maxLengthText / 2).toLowerCase();
    }

    public static String getAnchorEmployer(Employer e) {
        return getByLimit(join(" ", e.getName(), e.getAddress()), maxLengthText / 2).toLowerCase();
    }

    public static String getAnchorEmployer(VacancyTo vTo) {
        return getByLimit(join(" ", vTo.getEmployerName(), vTo.getAddress()), maxLengthText / 2).toLowerCase();
    }

    public static boolean isToValid(Freshen f, String text) {
        String temp = text.toLowerCase();
        return (temp.contains(f.getLanguage()) || isWorkerIT(temp)) && wasteSkills.stream().noneMatch(temp::contains);
    }
}
