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

public class JobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsStrategy.class);
    private static final String URL_FORMAT = "https://jobs.dou.ua/vacancies/?category=%s&search=%s&%s";
    private static final String URL_FORMAT_REMOTE = "https://jobs.dou.ua/vacancies/?category=%s&remote";
    // киев        https://jobs.dou.ua/vacancies/?category=Java&search=Middle&city=Киев
    // за рубежем  https://jobs.dou.ua/vacancies/?category=Java&search=middle&relocation=
    // удаленно   https://jobs.dou.ua/vacancies/?category=Java&remote

    protected Document getDocument(String city, String language, String position) {
        if(city.equals("удаленно")){
            return DocumentUtil.getDocument(format(URL_FORMAT_REMOTE, language));
        }
        city = city.equals("за_рубежем") ? "relocation=" : "".concat("city=").concat(city);
        return DocumentUtil.getDocument(format(URL_FORMAT, language, position, city));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city {} language {}", freshen.getWorkplace(), freshen.getLanguage());
        Set<VacancyTo> set = new LinkedHashSet<>();
        String[] positions = freshen.getWorkplace().equals("удаленно") ?
                new String[]{"middle", "developer", "java"} : new String[]{""};
        for(String p : positions) {
            Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage(), p);
            Elements elements = doc == null ? null : doc.getElementsByClass("vacancy");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesJobs(elements, freshen));
        }
        reCall(set.size(), new JobsStrategy());
        return new ArrayList<>(set);
    }
}
