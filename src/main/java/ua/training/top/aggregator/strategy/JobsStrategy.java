package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.collect.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesJobs;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.WorkplaceUtil.getJobs;

public class JobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsStrategy.class);
    private static final String url = "https://jobs.dou.ua/vacancies/?%scategory=%s%s";
    private static final String url_trainee = "https://jobs.dou.ua/first-job/?from=exp";
    // Київ        https://jobs.dou.ua/vacancies/?category=Java&city=Киев

    protected Document getDocument(String workplace, String language, String level) {
        return DocumentUtil.getDocument(format(url, getWorkplace(workplace), language, getLevel(jobs, level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        String[] cities = workplace.equals("foreign") || workplace.equals("россия") ? getRU() :
                workplace.equals("украина") || workplace.equals("all") ? getUA() : new String[]{workplace};
        Set<VacancyTo> set = new LinkedHashSet<>();
        for(String location : cities) {
        Document doc = level.equals("trainee") ? DocumentUtil.getDocument(url_trainee) :
                getDocument(getJobs(location), language, level);
        Elements elements = doc == null ? null : doc.getElementsByClass("vacancy");
        if (doc == null || elements == null) return new ArrayList<>();
        set.addAll(getVacanciesJobs(elements, freshen));
    }
        reCall(set.size(), new JobsStrategy());
        return new ArrayList<>(set);
    }

    private String getWorkplace(String workplace) {
        return workplace.equals("all") ? "" : workplace.equals("remote") || workplace.equals("relocation") ?
                getJoin(workplace,"&") : getJoin("city=",workplace,"&");
    }

    public static String[] getUA() {
        return new String[]{"украина", "киев", "харьков", "львов", "одесса", "днепр", "винница", "ужгород",
                "ивано-франковск", "полтава", "запорожье", "черкассы", "чернигов", "тернополь"};
    }

    public static String[] getRU() {
        return new String[]{"россия", "санкт-петербург", "москва", "новосибирск", "нижний новгород", "казань", "пермь",
                "екатеринбург", "краснодар", "ростов-на-дону", "томск", "самара", "ульяновск", "воронеж"};
    }
}
