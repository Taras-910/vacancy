package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.jsoup.DocumentUtil;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.jsoup.ElementUtil.getVacanciesJobs;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reCall;

public class JobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsStrategy.class);
    private static final String URL_FORMAT = "https://jobs.dou.ua/vacancies/?category=%s&search=%s&%s";
    //киев https://jobs.dou.ua/vacancies/?category=Java&search=Middle&city=Киев
    // за рубежем  https://jobs.dou.ua/vacancies/?category=Java&search=middle&relocation=

    protected Document getDocument(String city, String language, String position) {
        city = city.equals("за_рубежем") ? "relocation=" : "".concat("city=").concat(city);
        return DocumentUtil.getDocument(format(URL_FORMAT, language, position, city));
    }

    @Override
    public List<VacancyTo> getVacancies(DoubleString doubleString) throws IOException {
        log.info("getVacancies city {} language {}", doubleString.getWorkplaceTask(), doubleString.getLanguageTask());
        Set<VacancyTo> set = new LinkedHashSet<>();
        String[] positions = new String[]{"middle", "developer", "java"};
        for(String p : positions) {
            Document doc = getDocument(doubleString.getWorkplaceTask(), doubleString.getLanguageTask(), p);
            Elements elements = doc == null ? null : doc.getElementsByClass("vacancy");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesJobs(elements, doubleString));
        }
        reCall(set.size(), new JobsStrategy());
        return new ArrayList<>(set);
    }
}
