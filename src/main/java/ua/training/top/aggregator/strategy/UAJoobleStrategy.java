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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesJooble;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.LevelUtil.getLevel;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.*;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    private static final String url = "https://%sjooble.org/SearchResult?date=3%s%s&ukw=%s%s";
    //    https://ua.jooble.org/SearchResult?date=3&rgns=Польща&ukw=java

    protected Document getDocument(String country, String workplace, String language, String level, String page) {
        String city = workplace.equals("remote") ? getRemoteByCountry(country) : getCityByCountry(country, workplace);
        return DocumentUtil.getDocument(format(url,
                country.equals("us") || country.equals("il") ? "" : getJoin(country, "."),
                workplace.equals("all") ? "" : getJoin("&rgns=", city), getPage(jooble, page),
                language, getLevel(jooble, level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (isMatch(citiesRU, workplace)) {
            return new ArrayList<>();
        }
        String country = getCountryByCity(workplace);
        String[] workplaces = isMatch(foreignAria, workplace) ? getForeign() : new String[]{workplace};
        Set<VacancyTo> set = new LinkedHashSet<>();
        for (String location : workplaces) {
            int page = 1;
            while (true) {
                Document doc = getDocument(country, location, language, level, valueOf(page));
                Elements elements = doc == null ? null : doc.select("[data-test-name=_jobCard]");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesJooble(elements, freshen, country));
                if (page < getMaxPages(jooble, location)) page++;
                else break;
            }
        }
        reCall(set.size(), new UAJoobleStrategy());
        return new ArrayList<>(set);
    }

    public static String[] getForeign() {
        return new String[]{"Канада", "Польща", "Німеччина", "Швеція", "Ізраїль", "Швейцарія", "США", "Франція", "Італія",
                "Фінляндія", "Велика Британія", "ОАЕ", "Чехія", "Словаччина"};
    }
}
