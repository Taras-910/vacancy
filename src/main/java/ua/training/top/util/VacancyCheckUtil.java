package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

public class VacancyCheckUtil {
    public static Logger log = LoggerFactory.getLogger(VacancyCheckUtil.class) ;
    public static final String URL_MATCHER = "^(?!mailto:)(?:(?:http|https|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?:(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[0-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)(?:\\.(?:[a-z\\u00a1-\\uffff0-9]+-?)*[a-z\\u00a1-\\uffff0-9]+)*(?:\\.(?:[a-z\\u00a1-\\uffff]{2,})))|localhost)(?::\\d{2,5})?(?:(/|\\?|#)[^\\s]*)?$";
    public static final String EMAIL_MATCHER ="^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    public static final String EMAIL_ERROR_MESSAGE = "[email] должен иметь формат адреса электронной почты";
    public static final String URL_ERROR_MESSAGE = "[url] должен иметь формат URL или доменное имя";
    public static final String USER_EXIST_MESSAGE = "User with this meal already exist";
    public static final String LOGIN_MESSAGE = "?message=You are already registered. Please Sign in&username=";

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

    public static boolean getMatchesLanguage(Freshen f, String title, String skills){
        return f.getLanguage().equals("all")
                || title.toLowerCase().contains("рекрутер")|| title.toLowerCase().contains("recruiter")
                || title.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*")
                || skills.toLowerCase().matches(".*\\b"+f.getLanguage()+"\\b.*");
    }
}
