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
import static ua.training.top.util.parser.ElementUtil.getVacanciesYandex;
import static ua.training.top.util.parser.data.CorrectAddress.getCorrectWorkplaceYandex;

public class YandexStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(YandexStrategy.class);
    private static final String URL_FORMAT = "https://rabota.yandex.ru/%s/vakansii/?text=%s%s&top_days=7";
    private static final String URL_FORMAT_REMOTE = "https://rabota.yandex.ru/search/vakansii/rabota-udalennaya-i-na-domu/?text=%s&rid=10001&page_num=%s";

//             https://rabota.yandex.ru/kiev/vakansii/?text=java&page_num=2&top_days=7
// удаленная   https://rabota.yandex.ru/search/vakansii/rabota-udalennaya-i-na-domu/?text=java&rid=10001&page_num=2

    protected Document getDocument(String city, String language, String page) {
        page = page.equals("1") ? "" : "&page_num=".concat(page);
        return DocumentUtil.getDocument(city.equals("удаленная") ? format(URL_FORMAT_REMOTE, language, page) :
                format(URL_FORMAT, city, language, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        String workplace = getCorrectWorkplaceYandex(freshen.getWorkplace());
        log.info("workplace={}", workplace);
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, freshen.getLanguage(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("serp-vacancy");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesYandex(elements, freshen));
            if(page < limitCallPages) page++;
            else break;
        }
//        reCall(set.size(), new YandexStrategy());
        return new ArrayList<VacancyTo>(set);
    }
}
