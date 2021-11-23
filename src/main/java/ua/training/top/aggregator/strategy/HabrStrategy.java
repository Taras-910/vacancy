package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.collect.DocumentUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesHabr;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getHabr;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    private static final String url = "https://career.habr.com/vacancies?%s%sq=%s%s%s%s&sort=date&type=all";
    //https://career.habr.com/vacancies?city_id=679&page=2&q=java&qid=4&remote=true&sort=date&type=all

    protected Document getDocument(String workplace, String language, String level, String page) {
        String city = workplace.equals("all") || workplace.equals("remote") ? "" : "city_ids[]=".concat(workplace).concat("&");
        return DocumentUtil.getDocument(format(url, city, getPage(habr, page), language, getLevel(habr ,level),
                workplace.equals("remote") ? "&remote=true" : "", getPartUrlsHabr(language)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) {
        String workplace = getHabr(freshen.getWorkplace()), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        Set<VacancyTo> set = new LinkedHashSet<>();
        try {
            if((workplace).equals("-1")){
                return new ArrayList<>();
            }
            int page = 1;
            while (true) {
                Document doc = getDocument(workplace, language, level, valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("vacancy-card__inner");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesHabr(elements, freshen));
                if(page < getMaxPages(habr, freshen.getWorkplace())) page++;
                else break;
            }
        } catch (Exception e) {
            log.info(error, e.getMessage(), this.getClass().getSimpleName());
            return new ArrayList<>(set);
        }
        reCall(set.size(), new HabrStrategy());
        return new ArrayList<>(set);
    }

    public static String getPartUrlsHabr(String language) {
        String skills = switch (language) {
            case "php" -> "1005";
            case "ruby" -> "1081";
            case "javascript" -> "264";
            case "kotlin" -> "239";
            case "c#" -> "706";
            case "typescript" -> "245";
            case "c++" -> "172";
            default -> "1012";
        };
        return "&skills[]=".concat(skills);
    }
}
