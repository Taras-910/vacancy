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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesHabr;
import static ua.training.top.util.parser.data.CorrectAddress.getCityHabr;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelHabr;
import static ua.training.top.util.parser.data.DataUtil.habr;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    public static final int maxPages = 3;
    private static final String URL = "https://career.habr.com/vacancies?%s%sq=%s&qid=%s&%ssort=date&type=all";
//    https://career.habr.com/vacancies?city_id=679&page=2&q=java&qid=4&remote=true&sort=date&type=all

    protected Document getDocument(String workplace, String language, String level, String page) {
        String city = workplace.equals("remote") ? "remote=true&" : "city_id=".concat(workplace).concat("&");
        return DocumentUtil.getDocument(format(URL, city, page.equals("1") ? "" : "page=".concat(page).concat("&"),
                language, getLevelHabr(level), workplace.equals("remote") ? "remote=true&" : ""));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) {
        Set<VacancyTo> set = new LinkedHashSet<>();
        String workplace = getCityHabr(freshen.getWorkplace());
        try {
            if((workplace).equals("-1")){
                return new ArrayList<>();
            }
            int page = 1;
            while (true) {
                Document doc = getDocument(workplace, freshen.getLanguage(), freshen.getLevel(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("vacancy-card__inner");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesHabr(elements, freshen));
                if(page < getMaxPages(habr, workplace)) page++;
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
