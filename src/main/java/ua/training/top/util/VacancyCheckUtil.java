package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import static ua.training.top.util.MessageUtil.check_error_data;
import static ua.training.top.util.MessageUtil.vacancy_not_be_null;

public class VacancyCheckUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyCheckUtil.class) ;

    public static boolean isNotSimilar(Vacancy v, VacancyTo vTo) {
        return !v.getTitle().equals(vTo.getTitle()) ||
                !v.getEmployer().getName().equals(vTo.getEmployerName()) ||
                !v.getSkills().equals(vTo.getSkills());
    }

    public static void isNullPointerException(VacancyTo vacancyTo) {
        if(!checkNullDataVacancyTo(vacancyTo)) {
            throw new NullPointerException(vacancy_not_be_null + vacancyTo);
        }
    }

    public static boolean checkNullDataVacancyTo(VacancyTo v) {
        String[] line = {v.getTitle(), v.getEmployerName(), v.getAddress(), v.getSkills(), v.getUrl()};
        for(String text : line) {
            if (text == null || text.equals("")) {
                log.error(check_error_data, v);
                return false;
            }
        }
        return true;
    }
}
