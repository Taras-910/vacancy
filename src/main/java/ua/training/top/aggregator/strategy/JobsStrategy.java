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
    private static final String url_trainee = "https://jobs.dou.ua/first-job/?from=exp";
    private static final String url_in = "https://jobs.dou.ua/vacancies/%s%s%s%s%s%s";
    private static final String url_out = "https://relocate.dou.ua/jobs/%s%s%s%s";
    // https://relocate.dou.ua/jobs/?search=canada&descr=1&category=Java&exp=1-3

    protected Document getDocument(String workplace, String language, String level) {
        boolean domestic = isMatch(citiesUA, workplace) || isMatch(remoteAria, workplace) ||
                isMatch(foreignAria, workplace) || workplace.equals("all");
        return domestic ? DocumentUtil.getDocument(format(url_in,
                !workplace.equals("all") || !language.equals("all") || !level.equals("all") ? "?" : "",
                getWorkplace(workplace),
                !workplace.equals("all") && !language.equals("all") || !workplace.equals("all") && !level.equals("all") ? "&" : "",
                language.equals("all") ? "" : getJoin("category=", getUpperStart(language)),
                !language.equals("all") && !level.equals("all") ? "&" : "",
                getLevel(jobs, level)))
                : DocumentUtil.getDocument(format(url_out,
                !language.equals("all") || !level.equals("all") ? "?" : "",
                language.equals("all") ? "" : getJoin("category=",language),
                language.equals("all") || level.equals("all") ? "" : "&",
                level.equals("all") ? "" : getLevel(jobs, level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        String[] cities = workplace.equals("украина") ? getUA() : new String[]{workplace};
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
        return workplace.equals("all") ? "" : isMatch(remoteAria, workplace) ? "remote" :
                isMatch(foreignAria, workplace) ? "relocation" : getJoin("city=", workplace);
    }

    public static String[] getUA() {
        return new String[]{"украина", "киев", "харьков", "львов", "одесса", "днепр", "винница", "ужгород",
                "ивано-франковск", "полтава", "запорожье", "черкассы", "чернигов", "тернополь"};
    }
}
