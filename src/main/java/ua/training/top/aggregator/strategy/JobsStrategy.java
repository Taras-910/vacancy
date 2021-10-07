package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesJobs;
import static ua.training.top.util.parser.data.CorrectAddress.getCityJobs;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelJob;

public class JobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsStrategy.class);
    public static final int maxPages = 5;
    private static final String URL_FORMAT = "https://jobs.dou.ua/vacancies/?category=%s&%s%s";
    private static final String URL_INTERN = "https://jobs.dou.ua/first-job/?from=exp";
    // Київ        https://jobs.dou.ua/vacancies/?category=Java&city=Киев
    // Київ junior https://jobs.dou.ua/vacancies/?city=Киев&category=Java&exp=0-1
    // за рубежем  https://jobs.dou.ua/vacancies/?category=Java&relocation=
    // удаленно    https://jobs.dou.ua/vacancies/?category=Java&remote

    protected Document getDocument(String city, String language, String level) {
        city = city.equals("за_рубежем") ? "relocation" : city.equals("удаленно") ? "remote" : "city=".concat(city);
        return DocumentUtil.getDocument(level.equals("intern") ? URL_INTERN : format(URL_FORMAT, language, city, getLevelJob(level)));
    }
 //   https://jobs.dou.ua/vacancies/?remote&category=Java&exp=1-3
    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city {} language {}", freshen.getWorkplace(), freshen.getLanguage());
        Set<VacancyTo> set = new LinkedHashSet<>();
        Document doc = getDocument(getCityJobs(freshen.getWorkplace()), freshen.getLanguage(), freshen.getLevel());
        Elements elements = doc == null ? null : doc.getElementsByClass("vacancy");
        if (doc == null || elements == null) return new ArrayList<>();
        set.addAll(getVacanciesJobs(elements, freshen));
        reCall(set.size(), new JobsStrategy());
        return new ArrayList<>(set);
    }
}
