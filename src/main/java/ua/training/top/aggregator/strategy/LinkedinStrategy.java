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
import static ua.training.top.aggregator.jsoup.ElementUtil.getVacanciesLinkedin;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reCall;

public class LinkedinStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(LinkedinStrategy.class);
    private static final String URL_FORMAT = "https://www.linkedin.com/jobs/search?keywords=%s&location=%s&f_TP=1&redirect=false&position=1&pageNum=0";
    //агломерация киева https://www.linkedin.com/jobs/search?keywords=Java&location=Агломерация%2BКиева&f_TP=1%2C2&redirect=false&position=1&pageNum=0
    // за последнюю неделю f_TP=1%2C2
    // за последние 24 часа f_TP=1
    // https://www.linkedin.com/jobs/search?keywords=Java&location=Польша&f_TP=1%2C2&redirect=false&position=1&pageNum=0


    protected Document getDocument(String city, String language) {
        boolean other = city.equals("сша") || city.equals("польща") || city.equals("німеччина");
        String url = format(URL_FORMAT, language, city);
        return DocumentUtil.getDocument(url);
    }

    @Override
    public List<VacancyTo> getVacancies(DoubleString doubleString) throws IOException {
        log.info("getVacancies city {} language={}", doubleString.getWorkplaceTask(), doubleString.getLanguageTask());
        String[] countries = new String[]{"Канада", "Польша", "германия", "Швеция", "Израиль", "Швеция", "Соединенные%2BШтаты%2BАмерики"};
        String[] cityOrCountry = doubleString.getWorkplaceTask().contains("за_рубежем") ? countries : new String[]{doubleString.getWorkplaceTask()};
        List<VacancyTo> result = new ArrayList<>();
        Set<VacancyTo> set = new LinkedHashSet<>();
        for(String c : cityOrCountry) {
            Document doc = getDocument(c, doubleString.getLanguageTask());
            Elements elements = doc == null ? null : doc.getElementsByClass("result-card");
            set.addAll(getVacanciesLinkedin(elements, doubleString));
        }
        result.addAll(set);
        reCall(result.size(), new LinkedinStrategy());
        return result;
    }
}
