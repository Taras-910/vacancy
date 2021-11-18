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
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getJooble;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    private static final String url = "https://ua.jooble.org/SearchResult?date=3%s%s%s&ukw=%s%s";
    //    https://ua.jooble.org/SearchResult?date=3&rgns=Польща&ukw=java

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, workplace.equals("remote") ? "&loc=2" : "",
                getJooble(workplace), getPage(jooble, page), language, getLevel(jooble, level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (isCityRu(workplace)) {
            return new ArrayList<>();
        }
        String[] workplaces = workplace.equals("foreign") ?
                getForeign() : workplace.equals("украина") ? getUA() : new String[]{workplace};
        Set<VacancyTo> set = new LinkedHashSet<>();
        for (String location : workplaces) {
            int page = 1;
            while (true) {
                Document doc = getDocument(location, language, level, valueOf(page));
                Elements elements = doc == null ? null : doc.select("[data-test-name=_jobCard]");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesJooble(elements, freshen));
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

    public static String[] getUA() {
        return new String[]{"Київ", "Харків", "Львів", "Одеса", "Дніпро", "Вінниця", "Ужгород", "Івано-Франківськ",
                "Полтава", "Запоріжжя", "Черкаси", "Чернігів", "Тернопіль"};
    }
}
