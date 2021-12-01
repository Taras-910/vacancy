package ua.training.top.util.collect.data;

import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import static ua.training.top.util.collect.data.DataUtil.*;

public class ToUtil {

    public static VacancyTo getFilled(VacancyTo vacancyTo, Freshen freshen) {
        vacancyTo.setWorkplace(freshen.getWorkplace());
        vacancyTo.setLevel(freshen.getLevel());
        vacancyTo.setLanguage(freshen.getLanguage());
        vacancyTo.setToVote(false);
        return vacancyTo;
    }

    public static String getAnchorVacancy(Vacancy v) {
        return getBuild(v.getTitle()).append(v.getEmployer().getName()).append(v.getEmployer().getAddress())
                .append(v.getSkills()).toString().toLowerCase();
    }

    public static String getAnchorVacancy(VacancyTo vTo) {
        return getBuild(vTo.getTitle()).append(vTo.getEmployerName()).append(vTo.getAddress())
                .append(vTo.getSkills()).toString().toLowerCase();
    }

    public static String getAnchorEmployer(Employer e) {
        return getBuild(e.getName()).append(e.getAddress()).toString().toLowerCase();
    }

    public static String getAnchorEmployer(VacancyTo vTo) {
        return getBuild(vTo.getEmployerName()).append(vTo.getAddress()).toString().toLowerCase();
    }

    public static boolean isToValid(Freshen f, String text) {
        String temp = text.toLowerCase();
        return (temp.contains(f.getLanguage()) || isMatch(workersIT, temp)) && wasteSkills.stream().noneMatch(temp::contains);
    }
}
