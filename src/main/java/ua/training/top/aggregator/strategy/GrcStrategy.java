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
import static ua.training.top.util.parser.ElementUtil.getVacanciesGrc;
import static ua.training.top.util.parser.data.CorrectAddress.getCityGrc;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelGrc;

public class GrcStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(GrcStrategy.class);
    public static final int maxPages = 6;
    private static final String URL_FORMAT = "https://grc.ua/search/vacancy?clusters=true&ored_clusters=true&enable_snippets=true&is_part_time_clusters_enabled=true&search_period=7&st=searchVacancy&text=%s&%s&%s&search_field=description&search_field=company_name&search_field=name%s";
//киев/mid/7day
//https://grc.ua/search/vacancy?clusters=true&ored_clusters=true&enable_snippets=true&is_part_time_clusters_enabled=true&search_period=7&st=searchVacancy&text=java&area=115&experience=between1And3&search_field=description&search_field=company_name&search_field=name
//remote/junior/7day
//https://grc.ua/search/vacancy?clusters=true&ored_clusters=true&enable_snippets=true&is_part_time_clusters_enabled=true&search_period=7&st=searchVacancy&text=java&experience=noExperience&schedule=remote&search_field=description&search_field=company_name&search_field=name

    protected Document getDocument(String city, String language, String level, String page) {
        page = page.equals("0")? "" : "&page=".concat(page);
        return DocumentUtil.getDocument(city.equals("schedule=remote") ?
                format(URL_FORMAT, language, level, city, page) : format(URL_FORMAT, language, city, level, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String[] workplaces = freshen.getWorkplace().equals("за_рубежем") ?
                new String[]{"израиль", "сша", "германия", "швеция", "норвегия", "польша"} : new String[]{freshen.getWorkplace()};
        Set<VacancyTo> set = new LinkedHashSet<>();
        for(String city : workplaces) {
            int page = 0;
            while(page < 3) {
                Document doc = getDocument(getCityGrc(city), freshen.getLanguage(), getLevelGrc(freshen.getLevel()), String.valueOf(page));
                Elements elements = doc == null ?
                        null : doc.getElementsByAttributeValueStarting("data-qa","vacancy-serp__vacancy vacancy-serp__vacancy");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesGrc(elements, freshen));
                if(page < Math.min(limitCallPages, maxPages)) page++;
                else break;
            }
        }
        return new ArrayList<>(set);
    }
}
