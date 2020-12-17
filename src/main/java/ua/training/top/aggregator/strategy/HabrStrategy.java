package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.util.jsoup.DocumentUtil;
import ua.training.top.to.DoubleString;
import ua.training.top.to.VacancyTo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.util.installation.InstallationUtil.reCall;
import static ua.training.top.aggregator.util.jsoup.ElementUtil.getVacanciesHabr;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    private static final String URL_FORMAT = "https://career.habr.com/vacancies?city_id=%s&q=%s&sort=date&type=all";
    // by date https://career.habr.com/vacancies?city_id=908&q=java&sort=date&type=all

    protected Document getDocument(String city, String language) {
        String cityId = "908"; // киев
        if(city.equals("санкт-петербург")) cityId = "679";
        return DocumentUtil.getDocument(format(URL_FORMAT, cityId, language));
    }

    @Override
    public List<VacancyTo> getVacancies(DoubleString doubleString) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        if(doubleString.getWorkplaceTask().contains("за_рубежем")){
            return new ArrayList<>();
        }
        Document doc = getDocument(doubleString.getWorkplaceTask(), doubleString.getLanguageTask());
        Elements elements = doc == null ? null : doc.getElementsByClass("vacancy-card__inner");
        if (elements != null) {
            set.addAll(getVacanciesHabr(elements, doubleString));
        }
        reCall(set.size(), new HabrStrategy());
        return new ArrayList<>(set);
    }
}
