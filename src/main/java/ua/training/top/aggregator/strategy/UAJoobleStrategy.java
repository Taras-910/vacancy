package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.util.jsoup.DocumentUtil;
import ua.training.top.to.VacancyNet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.util.ProviderUtil.limitCallPages;
import static ua.training.top.aggregator.util.ProviderUtil.reCall;
import static ua.training.top.aggregator.util.jsoup.ElementUtil.getVacanciesJooble;

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
    public List<VacancyNet> getVacancies(String city, String language){
        log.info("getVacancies city {} language={}", city, language);
        boolean other = city.equals("за_рубежем");
        String[] cityOrCountry = other ? new String[]{"сша", "польща", "німеччина"} : new String[]{city};
        List<VacancyNet> rezult = new ArrayList<>();
        for(String c : cityOrCountry) {
            Set<VacancyNet> set = new LinkedHashSet<>();
            String tempDoc = "";
            int page = 1;
            int limitEmptyDoc = 3;
            while (true) {
                Document doc = getDocument(c, language, String.valueOf(page));
//                Elements elements = doc == null ? null : doc.getElementsByAttribute("data-sgroup");
//                Elements elements = doc == null ? null : doc.getElementsByClass("left-static-block");
                Elements elements = doc == null ? null : doc.getElementsByClass("vacancy_wrapper");

                if (elements == null || elements.size() == 0 || tempDoc.equals(doc.text())) {
                    limitEmptyDoc --;
                }
                if (limitEmptyDoc == 0) break;
                tempDoc = doc.text();
                set.addAll(getVacanciesJooble(elements));
                if (page < limitCallPages) page++;
                else break;
            }
            rezult.addAll(set);
        }
        reCall(rezult.size(), new UAJoobleStrategy());
        return rezult;
    }
}
