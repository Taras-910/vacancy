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
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesJooble;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    private static final String URL_FORMAT = "https://ua.jooble.org/SearchResult?ukw=%s&rgns=%s&p=%s";
    private static final String URL_FORMAT_REMOTE = "https://ua.jooble.org/SearchResult?loc=2&p=%s&ukw=%s";
    //за 7 дней            https://ua.jooble.org/SearchResult?ukw=java&rgns=Київ&date=3&p=1
    // за кордоном за 7 дн https://ua.jooble.org/SearchResult?ukw=java&rgns=за+кордоном&date=3&p=1
    //                     https://ua.jooble.org/SearchResult?date=3&loc=2&rgns=%D0%B7%D0%B0%20%D0%BA%D0%BE%D1%80%D0%B4%D0%BE%D0%BD%D0%BE%D0%BC&ukw=java
    // удаленно            https://ua.jooble.org/SearchResult?loc=2&p=2&ukw=java

    protected Document getDocument(String city, String language, String page) {
        if(city.equals("удаленно")) {
            return DocumentUtil.getDocument(format(URL_FORMAT_REMOTE, page, language));
        }
        boolean other = city.equals("сша") || city.equals("польща") || city.equals("німеччина");
        String url = format(URL_FORMAT, language, city, page = other ? page : "".concat(page).concat("&date=3"));
        return DocumentUtil.getDocument(url);
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen doubleString) throws IOException {
        log.info("getVacancies city {} language={}", doubleString.getWorkplace(), doubleString.getLanguage());
        boolean other = doubleString.getWorkplace().contains("за_рубежем");
        String[] cityOrCountry = other ? new String[]{"сша", "польща", "німеччина"} : new String[]{doubleString.getWorkplace()};
        List<VacancyTo> result = new ArrayList<>();
        for(String c : cityOrCountry) {
            Set<VacancyTo> set = new LinkedHashSet<>();
            String tempDoc = "";
            int page = 1;
            int limitEmptyDoc = 1;
            while (true) {
                Document doc = getDocument(c, doubleString.getLanguage(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.select("[data-test-name=_jobCard]");
                if (elements == null || elements.size() == 0 || tempDoc.equals(doc.text())) {
                    limitEmptyDoc --;
                }
                if (limitEmptyDoc == 0) break;
                tempDoc = doc.text();
                set.addAll(getVacanciesJooble(elements, doubleString));
                if (page < limitCallPages) page++;
                else break;
            }
            result.addAll(set);
        }
        reCall(result.size(), new UAJoobleStrategy());
        return result;
    }
}
