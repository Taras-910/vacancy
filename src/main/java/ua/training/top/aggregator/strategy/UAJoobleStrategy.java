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
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesJooble;
import static ua.training.top.util.parser.data.CorrectAddress.getCorrectWorkplaceJooble;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    private static final String URL_FORMAT = "https://ua.jooble.org/SearchResult?ukw=%s&rgns=%s&p=%s";
    private static final String URL_FORMAT_REMOTE = "https://ua.jooble.org/SearchResult?loc=2&p=%s&ukw=%s";
    //за 7 дней            https://ua.jooble.org/SearchResult?ukw=java&rgns=Київ&date=3&p=1
    // за кордоном за 7 дн https://ua.jooble.org/SearchResult?ukw=java&rgns=за+кордоном&date=3&p=1
    //                     https://ua.jooble.org/SearchResult?date=3&loc=2&rgns=%D0%B7%D0%B0%20%D0%BA%D0%BE%D1%80%D0%B4%D0%BE%D0%BD%D0%BE%D0%BC&ukw=java
    // удаленно            https://ua.jooble.org/SearchResult?loc=2&p=2&ukw=java
    // Мінськ Білорусь     https://ua.jooble.org/SearchResult?rgns=%D0%9C%D1%96%D0%BD%D1%81%D1%8C%D0%BA%2C%20%D0%91%D1%96%D0%BB%D0%BE%D1%80%D1%83%D1%81%D1%8C&ukw=java
//          String url7 = "https://ua.jooble.org/desc/jdp/-9161132033114422761/DevOps-Engineer-%D0%9A%D0%B8%D1%97%D0%B2?ckey=java&amp";
//                         https://ua.jooble.org/SearchResult?date=3&p=1&rgns=%D0%9A%D0%B8%D1%97%D0%B2&ukw=java
//                         https://ua.jooble.org/jdp/-7570587103435261
    protected Document getDocument(String city, String language, String page) {
        if(city.equals("удаленно")) {
            return DocumentUtil.getDocument(format(URL_FORMAT_REMOTE, page, language));
        }
        boolean other = city.equals("сша") || city.equals("польща") || city.equals("німеччина");
        String url = format(URL_FORMAT, language, city, page = other ? page : "".concat(page).concat("&date=3"));
        return DocumentUtil.getDocument(url);
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = getCorrectWorkplaceJooble(freshen.getWorkplace());
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        log.info("getVacancies city {} language={}", freshen.getWorkplace(), freshen.getLanguage());
        boolean other = workplace.equals("за_рубежем");
        String[] cityOrCountry = other ? new String[]{"сша", "польща", "німеччина"} : new String[]{workplace};
        List<VacancyTo> result = new ArrayList<>();
        for(String c : cityOrCountry) {
            Set<VacancyTo> set = new LinkedHashSet<>();
            String tempDoc = "";
            int page = 1;
            int limitEmptyDoc = 1;
            while (true) {
                Document doc = getDocument(c, freshen.getLanguage(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.select("[data-test-name=_jobCard]");
                if (elements == null || elements.size() == 0 || tempDoc.equals(doc.text())) {
                    limitEmptyDoc --;
                }
                if (limitEmptyDoc == 0) break;
                tempDoc = doc.text();
                set.addAll(getVacanciesJooble(elements, freshen));
                if (page < limitCallPages) page++;
                else break;
            }
            result.addAll(set);
        }
        reCall(result.size(), new UAJoobleStrategy());
        return result;
    }
}
