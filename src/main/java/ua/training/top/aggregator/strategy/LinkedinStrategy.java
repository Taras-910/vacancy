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
//    private static final String URL_FORMAT = "https://www.linkedin.com/jobs/search?keywords=%s&location=%s&f_TP=%s&redirect=false&position=1&pageNum=%s";
//    private static final String URL_FORMAT = "https://www.linkedin.com/jobs/search?keywords=%s&location=%s&trk=public_jobs_jobs-search-bar_search-submit&redirect=false&position=1&pageNum=%s&f_TP=1&f_E=4";
    private static final String URL_FORMAT = "https://ru.linkedin.com/jobs/search?keywords=%s&location=%s&trk=public_jobs_jobs-search-bar_search-submit&redirect=false&position=1&pageNum=%s&f_TP=%s&f_E=4";
    //агломерация киева https://www.linkedin.com/jobs/search?keywords=Java&location=Агломерация%2BКиева&f_TP=1%2C2&redirect=false&position=1&pageNum=0
    // за последнюю неделю f_TP=1%2C2
    // за последние 24 часа f_TP=1

    protected Document getDocument(String city, String language, String page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city, page, "1%2C2"));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String[] countries = getLinkedin();
        String[] cityOrCountry = freshen.getWorkplace().contains("за_рубежем") ? countries : new String[]{freshen.getWorkplace()};
        List<VacancyTo> result = new ArrayList<>();
        Set<VacancyTo> set = new LinkedHashSet<>();
        for(String c : cityOrCountry) {
            int page = 0;
            while(page < 5) {
                Document doc = getDocument(c, freshen.getLanguage(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("result-card");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesLinkedin(elements, freshen));
                page = cityOrCountry.length == 1 ? page + 1 : page + 5;
            }
        }
        result.addAll(set);
        reCall(result.size(), new LinkedinStrategy());
        return result;
    }

    public static final  String[] getLinkedin() {
        return new String[]{"Канада&geoId=101174742", "Польша&geoId=105072130", "Германия&geoId=101282230",
                "Швеция&geoId=105117694", "Израиль&geoId=101620260", "Швейцарская%20Конфедерация&geoId=106693272",
                "Соединенные%2BШтаты%2BАмерики&geoId=103644278", "Франция&geoId=105015875", "Италия&geoId=103350119",
                "Финляндия&geoId=100456013", "Сингапур&geoId=102454443", "Соединённое%20Королевство&geoId=101165590",
                "Объединенные%20Арабские%20Эмираты&geoId=104305776"};
    }
}
