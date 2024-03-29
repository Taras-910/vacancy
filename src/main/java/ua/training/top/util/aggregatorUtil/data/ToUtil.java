package ua.training.top.util.aggregatorUtil.data;

import ua.training.top.model.Employer;
import ua.training.top.model.Freshen;
import ua.training.top.model.Vacancy;
import ua.training.top.to.VacancyTo;

import static ua.training.top.aggregator.InstallationUtil.maxLengthText;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;

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
        return (f.getLanguage().equals("all") ? allLanguages.stream().anyMatch(temp::contains) : temp.contains(f.getLanguage())
                || isMatch(workersIT, temp)) && wasteSkills.stream().noneMatch(temp::contains);
    }

    public static String getToTitle(String title) {
        return isEmpty(title) ? link : getUpperStart(correctToLanguage(title));
    }

    public static String getToName(String compName) {
        return isEmpty(compName) ? link : isContains(compName, ",") ? compName.split(",")[0].trim() : compName;
    }

    public static String getToSkills(String skills, Freshen freshen) {
        if (isEmpty(skills)) {
            return freshen.getLanguage().equals("all") ? link : getUpperStart(freshen.getLanguage());
        }
        skills = isContains(skills, "Experience level:") ? skills.substring(skills.indexOf("Experience level:")) : skills;
        return correctToLanguage(skills.length() > maxLengthText ? skills.substring(0, maxLengthText) : skills);
    }

    public static String correctToLanguage(String text) {
        return isContains(text, "Java Script") ? text.replaceAll("Java Script", "JavaScript") : text;
    }

    public static String getToUrl(String site, String url){
        String prefix = switch (site) {
            case djinni -> "https://djinni.co";
            case cwjobs -> "https://www.cwjobs.co.uk/";
            case nofluff -> "https://nofluffjobs.com";
            case indeed -> "https://ua.indeed.com/viewjob?jk=";
            case indeed_ca -> "https://ca.indeed.com/viewjob?jk=";
            case jooble -> "jooble.org/desc/";
            case rabota -> "https://rabota.ua";
            case reed -> "https://www.reed.co.uk/";
            case work -> "https://www.work.ua";
            case jobBank -> "https://www.jobbank.gc.ca";
            case itJobsWatch -> "https://www.itjobswatch.co.uk";
            default -> "";
        };
        return getJoin(prefix,url);
    }
}
