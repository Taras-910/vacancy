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
import static ua.training.top.util.collect.ElementUtil.getNofluffjobsVacancies;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.LevelUtil.getLevel;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getPl;
import static ua.training.top.util.collect.data.WorkplaceUtil.getUA_en;

public class NofluffjobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(NofluffjobsStrategy.class);
    private final static String part = "%3Dtesting", part_language = "%20requirement%3D",
            url = "https://nofluffjobs.com/%s/%sbackend?criteria=category%s%s%s%s";
// https://nofluffjobs.com/pl/warszawa/backend?criteria=category%3Dtesting%20seniority%3Dmid%20requirement%3DJava&page=1

    protected Document getDocument(String workplace, String page, String level, String language) {
        String city = isMatch(citiesPl, workplace) ? getPl(workplace).toLowerCase() : getUA_en(workplace).toLowerCase();
        return DocumentUtil.getDocument(format(url, isMatch(citiesUA, uaAria, workplace) ? "ua" : "pl",
                isMatch(remoteAria, workplace) ? "praca-zdalna/" : isMatch(foreignAria, of("all"), workplace) ?
                        "" : getJoin(city.equals("kyiv") ? "kiev" : city, "/"),
                part, getLevel(nofluff, level), language.equals("all") ? "" : getJoin(part_language, language),
                getPage(nofluff, page)));
    }
    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (!isMatch(citiesPl, citiesUA, workplace) && isMatch(plAria, uaAria, workplace)) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByClass("posting-list-item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getNofluffjobsVacancies(elements, freshen));
            if (page < getMaxPages(nofluff, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new NofluffjobsStrategy());
        return new ArrayList<>(set);
    }

    public static String getToNofluffAddress(String address) {
        address = address.equals("") ? "Польша" : address.replaceAll("Zdalna", "Remote");
        return address;
    }
}
