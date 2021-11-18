package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.collect.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ua.training.top.util.collect.ElementUtil.getVacanciesGrc;
import static ua.training.top.util.collect.data.DataUtil.get_vacancy;
import static ua.training.top.util.collect.data.DataUtil.grc;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getGrc;

public class GrcStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(GrcStrategy.class);
    public static final String
            url_foreign = "https://grc.ua/search/vacancy?clusters=true&ored_clusters=true&enable_snippets=true&is_part_time_clusters_enabled=true&search_period=7&text=Java&from=suggest_post&area=74&area=1001&area=85&area=236&area=13&search_field=description&search_field=company_name&search_field=name",
            url = "https://grc.ua/search/vacancy?clusters=true%s&ored_clusters=true&enable_snippets=true&is_part_time_clusters_enabled=true&search_period=7&text=%s&from=suggest_post%s%s&search_field=description&search_field=company_name&search_field=name%s";
    // https://grc.ua/search/vacancy?clusters=true&area=2&ored_clusters=true&enable_snippets=true&is_part_time_clusters_enabled=true&search_period=7&text=java&from=suggest_post&experience=between1And3&search_field=description&search_field=company_name&search_field=name

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, workplace.equals("&schedule=remote") || workplace.equals("all") ?
                        "" : "&area=".concat(workplace), language, level.isEmpty() ? "" : level,
                workplace.equals("&schedule=remote") ? workplace : "", getPage(grc, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 0;
        while (true) {
            Document doc = workplace.equals("foreign") ? DocumentUtil.getDocument(url_foreign) :
                    getDocument(getGrc(workplace), language, getLevel(grc, level), valueOf(page));
            Elements elements = doc == null ?
                    null : doc.getElementsByAttributeValueStarting("data-qa", "vacancy-serp__vacancy vacancy-serp__vacancy");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesGrc(elements, freshen));
            if (page < getMaxPages(grc, freshen.getWorkplace())) page++;
            else break;
        }
        return new ArrayList<>(set);
    }
}
