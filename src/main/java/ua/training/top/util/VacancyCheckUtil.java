package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

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

    public static boolean getMatchesFreshen(Freshen f, String title, String skills){
        String language = f.getLanguage();
        return language.equals("all")
                || title.toLowerCase().matches(".*\\b"+language+"\\b.*")
                || skills.toLowerCase().matches(".*\\b"+language+"\\b.*");
    }
}
