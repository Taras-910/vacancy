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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesJooble;
import static ua.training.top.util.parser.data.CorrectAddress.getCityJooble;
import static ua.training.top.util.parser.data.CorrectAddress.isMatchesRu;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelJooble;

public class UAJoobleStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAJoobleStrategy.class);
    /*за неделю*/
//    private static final String URL = "https://ua.jooble.org/SearchResult?date=3&%s%s&ukw=%s%s";
    /*за 24 часа*/
//    private static final String URL = "https://ua.jooble.org/SearchResult?date=8&%s%s&ukw=%s%s";
    /*за все время*/
    private static final String URL = "https://ua.jooble.org/SearchResult?%s%s&ukw=%s%s";
//    https://ua.jooble.org/SearchResult?date=3&rgns=Польща&ukw=java
//    https://ua.jooble.org/SearchResult?date=8&loc=2&p=2&ukw=java

    protected Document getDocument(String city, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL, city, page.equals("1") ? "" : "&p=".concat(page), language, getLevelJooble(level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace();
        log.info("getVacancies workplace={} language={}", workplace, freshen.getLanguage());
        if (!isMatchesRu(freshen.getWorkplace())) {
            return new ArrayList<>();
        }
        String[] workplaces = workplace.equals("foreign") ? getForeignJooble() : new String[]{workplace};
        List<VacancyTo> result = new ArrayList<>();
        Set<VacancyTo> set = new LinkedHashSet<>();
        for(String location : workplaces) {
            int page = 1;
//            while(getLimitJooble(freshen) ? page < 3 : page < 10) {
            while(getLimitJooble(freshen) ? page < 3 : page < 4) {
                Document doc = getDocument(getCityJooble(location), freshen.getLanguage(), freshen.getLevel(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.select("[data-test-name=_jobCard]");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesJooble(elements, freshen));
                page = workplaces.length == 1 ? page + 1 : page + 10;
            }
        }
        result.addAll(set);
        reCall(result.size(), new UAJoobleStrategy());
        return result;
    }

    public static boolean getLimitJooble(Freshen freshen) {
        return freshen.getWorkplace().equals("foreign") || freshen.getLevel().equals("trainee") || freshen.getLevel().equals("junior");
    }

    public static String[] getForeignJooble() {
        return new String[]{"Канада", "Польща", "Німеччина", "Швеція", "Ізраїль", "Швейцарія", "США", "Франція", "Італія",
                "Фінляндія", "Велика Британія", "ОАЕ", "Чехія"};
    }
}
