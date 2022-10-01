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
import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesJooble;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.HelpUtil.*;
import static ua.training.top.util.collect.data.LevelUtil.getLevel;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getCityByCodeISO;
import static ua.training.top.util.collect.data.WorkplaceUtil.getCodeISOByCity;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    private static final String url = "https://%sjooble.org/SearchResult?date=3%s%s%s%s";
    //    https://ua.jooble.org/SearchResult?date=3&rgns=Польща&ukw=java

    protected Document getDocument(String codeISO, String workplace, String language, String level, String page) {
        String city = getCityByCodeISO(codeISO, workplace);
        return DocumentUtil.getDocument(format(url,
                isMatch((of("us", "il", "all")), codeISO) ? "" : getJoin(codeISO, "."),
                codeISO.equals("all") || workplace.equals("all") || isEmpty(city) ? "" : getJoin("&rgns=", city),
                getPage(jooble, page),
                language.equals("all") ? "" : getJoin("&ukw=", language),
                getLevel(jooble, level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();

        log.info(get_vacancy, workplace, language);
        if (isMatches(of(citiesRU, ilAria, citiesIl), workplace)) {
            return new ArrayList<>();
        }
        String[] workplaces = isMatch(foreignAria, workplace) ? getForeign() : new String[]{workplace};
        Set<VacancyTo> set = new LinkedHashSet<>();
        for (String location : workplaces) {
            String codeISO = getCodeISOByCity(location);
            int page = 1;
            while (true) {
                Document doc = getDocument(codeISO, location, language, level, valueOf(page));
                Elements elements = doc == null ? null : doc.select("[data-test-name=_jobCard]");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesJooble(elements, freshen, codeISO));
                if (page < getMaxPages(jooble, location)) page++;
                else break;
            }
        }
        reCall(set.size(), new UAJoobleStrategy());
        return new ArrayList<>(set);
    }

    public static String[] getForeign() {
        return new String[]{"Канада", "Польща", "Німеччина", "Швеція", "Швейцарія", "США", "Франція", "Італія",
                "Фінляндія", "Велика Британія", "ОАЭ", "Чехія", "Словаччина"};
    }
}
