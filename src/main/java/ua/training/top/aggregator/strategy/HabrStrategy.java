package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesHabr;
import static ua.training.top.util.parser.data.CorrectAddress.getCodeHabr;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    private static final String URL_FORMAT = "https://career.habr.com/vacancies?%sq=%s%s&sort=date&type=all";
//    можно удаленно  https://career.habr.com/vacancies?page=2&q=java&remote=true&sort=date&type=all
//    санкт-петербург https://career.habr.com/vacancies?city_id=679&page=2&q=java&sort=date&type=all
//                    https://career.habr.com/vacancies?page=2&q=java&city_id=679&sort=date&type=all

    protected Document getDocument(String city, String language, String page) {
        page = page.equals("1") ? "" : "page=".concat(page).concat("&");
        city = city.equals("удаленно") ? "&remote=true" : "&city_id=".concat(city);
        return DocumentUtil.getDocument(format(URL_FORMAT, page, language, city));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) {
        Set<VacancyTo> set = new LinkedHashSet<>();
        try {
            String city = freshen.getWorkplace();
            city = city.equals("удаленно") ? city : getCodeHabr(city);
            if(getCodeHabr(city).equals("-1")){
                return new ArrayList<>();
            }
            int page = 1;
            while (true) {
                Document doc = getDocument(city, freshen.getLanguage(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("vacancy-card__inner");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesHabr(elements, freshen));
                if(page < limitCallPages) page++;
                else break;
            }
        } catch (Exception e) {
            log.info("There is fault on 'HabrStrategy' because of {}", e.getLocalizedMessage());
            return new ArrayList<>(set);
        }
        reCall(set.size(), new HabrStrategy());
        return new ArrayList<>(set);
    }
}
