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
    //за 7 дней https://ua.jooble.org/SearchResult?ukw=java&rgns=Київ&date=3&p=1
    // за кордоном за 7 дн https://ua.jooble.org/SearchResult?ukw=java&rgns=за+кордоном&date=3&p=1

    protected Document getDocument(String city, String language, String page) {
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
