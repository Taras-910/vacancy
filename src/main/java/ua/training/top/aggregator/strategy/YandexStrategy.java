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
import static ua.training.top.util.parser.ElementUtil.getVacanciesYandex;
import static ua.training.top.util.parser.data.CorrectAddress.getCityYandex;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelYandex;
import static ua.training.top.util.parser.data.DataUtil.yandex;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;

public class YandexStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(YandexStrategy.class);
    public static final int maxPages = 5;
    private static final String URL = "https://o.yandex.ru/%s/vakansii%s/?text=%s%s%s&top_days=7%s";
//    https://rabota.yandex.ru/sankt-peterburg/vakansii/rabota-udalennaya-i-na-domu/?text=java%20intern &page_num=2&top_days=7&experience=FROM_1_TO_2
//    https://rabota.yandex.ru/%s/vakansii%s/?text=%s%s%s&top_days=7%s

    protected Document getDocument(String city, String language, String page, String level) {
        return DocumentUtil.getDocument(format(URL, city.equals("remote") ? "ukraina" : city,
                city.equals("remote") ? "/rabota-udalennaya-i-na-domu" : "", language,
                level.equals("trainee") ? "%20intern" : "",
                page.equals("1") ? "" : "&page_num=".concat(page), getLevelYandex(level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        String workplace = getCityYandex(freshen.getWorkplace());
        log.info("workplace={}", workplace);
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, freshen.getLanguage(), String.valueOf(page), freshen.getLevel());
            Elements elements = doc == null ? null : doc.getElementsByClass("OfferListingContent__listing__13zme");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesYandex(elements, freshen));
            if(page < getMaxPages(yandex, workplace)) page++;
            else break;
        }
        reCall(set.size(), new YandexStrategy());
        return new ArrayList<>(set);
    }
}
