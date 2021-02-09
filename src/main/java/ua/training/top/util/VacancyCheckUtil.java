package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.stream.Collectors;

public class VacancyCheckUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyCheckUtil.class) ;

    public static boolean isNotSimilar(Vacancy v, VacancyTo vTo) {
        return !v.getTitle().equals(vTo.getTitle()) ||
                !v.getEmployer().getName().equals(vTo.getEmployerName()) ||
                !v.getSkills().equals(vTo.getSkills());
    }

    public static void isNullPointerException(VacancyTo vacancyTo) {
        if(!checkNullDataVacancyTo(vacancyTo)) {
            throw new NullPointerException("data not be null" + vacancyTo);
        }
    }

    public static boolean checkNullDataVacancyTo(VacancyTo v) {
        String[] line = {v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSkills(), v.getUrl()};
        for(String text : line) {
            if (text == null || text.equals("")) {
                log.error("Error null data of vacancy {}", v);
                return false;
            }
        }
        return true;
    }

    public static List<Vacancy> getMatchesByFreshen(List<Vacancy> vacancies, Freshen freshen){
        return vacancies.stream()
                .filter(vacancy -> getMatchesFreshen(freshen, vacancy.getTitle(), vacancy.getSkills()))
                .collect(Collectors.toList());
    }

    public static boolean getMatchesFreshen(Freshen f, String title, String skills){
        return f.getLanguage().equals("all")
                || title.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*")
                || skills.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*");
    }
}
