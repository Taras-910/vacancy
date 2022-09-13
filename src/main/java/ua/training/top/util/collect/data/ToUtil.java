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
        return getJoin(v.getTitle(),v.getEmployer().getName(),v.getEmployer().getAddress(),v.getSkills()).toLowerCase();
    }

    public static String getAnchorVacancy(VacancyTo vTo) {
        return getJoin(vTo.getTitle(),vTo.getEmployerName(),vTo.getAddress(),vTo.getSkills()).toLowerCase();
    }

    public static String getAnchorEmployer(Employer e) {
        return getJoin(e.getName(),e.getAddress()).toLowerCase();
    }

    public static String getAnchorEmployer(VacancyTo vTo) {
        return getJoin(vTo.getEmployerName(),vTo.getAddress()).toLowerCase();
    }

    public static boolean isToValid(Freshen f, String text) {
        String temp = text.toLowerCase();
        return (temp.contains(f.getLanguage()) || isMatch(workersIT, temp)) && wasteSkills.stream().noneMatch(temp::contains);
    }
}
