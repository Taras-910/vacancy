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
import static ua.training.top.util.parser.ElementUtil.getVacanciesLinkedin;

public class LinkedinStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(LinkedinStrategy.class);
    private static final String URL_FORMAT = "https://www.linkedin.com/jobs/search?keywords=%s&location=%s&f_TP=%s&redirect=false&position=1&pageNum=%s";
    //агломерация киева https://www.linkedin.com/jobs/search?keywords=Java&location=Агломерация%2BКиева&f_TP=1%2C2&redirect=false&position=1&pageNum=0
    // за последнюю неделю f_TP=1%2C2
    // за последние 24 часа f_TP=1
    // https://www.linkedin.com/jobs/search?keywords=Java&location=Польша&f_TP=1&redirect=false&position=1&pageNum=0

    protected Document getDocument(String city, String language, String page) {
        boolean other = city.equals("сша") || city.equals("польща") || city.equals("німеччина");
        String url = format(URL_FORMAT, language, city, "1%2C2", page);
        return DocumentUtil.getDocument(url);
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String[] countries = new String[]{"Канада", "Польша", "германия", "Швеция", "Израиль", "Швеция", "Соединенные%2BШтаты%2BАмерики"};
        String[] cityOrCountry = freshen.getWorkplace().contains("за_рубежем") ? countries : new String[]{freshen.getWorkplace()};
        List<VacancyTo> result = new ArrayList<>();
        Set<VacancyTo> set = new LinkedHashSet<>();
        for(String c : cityOrCountry) {
            int page = 0;
            while(page < 5) {
                Document doc = getDocument(c, freshen.getLanguage(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("result-card");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesLinkedin(elements));
                page = cityOrCountry.length == 1 ? page + 1 : page + 5;
            }
        }
        result.addAll(set);
        reCall(result.size(), new LinkedinStrategy());
        return result;
    }
}
