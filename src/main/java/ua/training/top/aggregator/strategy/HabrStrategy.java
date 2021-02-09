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
import static ua.training.top.util.parser.data.CorrectAddress.getCodeHabr;

public class HabrStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(HabrStrategy.class);
    private static final String URL_FORMAT = "https://career.habr.com/vacancies?city_id=%s&q=%s&sort=date&type=all";
    // by date https://career.habr.com/vacancies?city_id=908&q=java&sort=date&type=all

    protected Document getDocument(String city, String language) {
        return DocumentUtil.getDocument(format(URL_FORMAT, getCodeHabr(city), language));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) {
        Set<VacancyTo> set = new LinkedHashSet<>();
        try {
            if(freshen.getWorkplace().contains("за_рубежем")){
                return new ArrayList<>();
            }
            Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage());
            Elements elements = doc == null ? null : doc.getElementsByClass("vacancy-card__inner");
            if (elements != null) {
                set.addAll(getVacanciesHabr(elements, freshen));
            }
            reCall(set.size(), new HabrStrategy());
        } catch (Exception e) {
            log.info("There is fault on 'HabrStrategy' because of {}", e.getLocalizedMessage());
            return new ArrayList<>(set);
        }
        return new ArrayList<>(set);
    }
}
