package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import java.util.ArrayList;
import java.util.List;

public class VacancyCheckUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyCheckUtil.class) ;

    public static boolean isNotSimilar(Vacancy v, VacancyTo vTo) {
        return !v.getTitle().equals(vTo.getTitle()) ||
                !v.getEmployer().getName().equals(vTo.getEmployerName()) ||
                !v.getSkills().equals(vTo.getSkills());
    }

    public static List<Vacancy> checkEmptyList(List<Vacancy> list, Freshen f) {
        if (list.isEmpty()) {
            log.error("database has not suitable vacancies for query: {"+ f.getLanguage() + ", "+ f.getWorkplace() + "}");
            return new ArrayList<>();
        }
        return list;
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

    public static boolean filterWorkplace(String workplace, Vacancy vacancy) {
        return workplace.equals("all") || vacancy.getFreshen().getWorkplace().contains(workplace) ||
                vacancy.getEmployer().getAddress().toLowerCase().contains(workplace);
    }

    public static boolean filterLanguage(String language, Vacancy vacancy) {
        return language.equals("all") || vacancy.getFreshen().getLanguage().contains(language) ||
                vacancy.getTitle().toLowerCase().contains(language) || vacancy.getSkills().toLowerCase().contains(language);
    }

    public static boolean filterJavaCase(String language, Vacancy vacancy) {
//        return !language.equals("java") || vacancy.getTitle().contains(" java ") || vacancy.getSkills().contains(" java ");
        if (language.equals("java")){
            return (!vacancy.getTitle().contains("javascript") && !vacancy.getSkills().contains("javascript")) ||
                    (vacancy.getTitle().contains(" java ") || vacancy.getSkills().contains(" java "));
        }
        return true;
    }
}
