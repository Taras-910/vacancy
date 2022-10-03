package ua.training.top.aggregator.strategies;

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
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getNofluffjobsVacancies;
import static ua.training.top.util.collect.data.CommonUtil.*;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.LevelUtil.getLevel;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getCityByCodeISO;
import static ua.training.top.util.collect.data.WorkplaceUtil.getCodeISOByCity;

public class NofluffjobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(NofluffjobsStrategy.class);
    private final static String part = "%3Dtesting", part_language = "%20requirement%3D",
            url = "https://nofluffjobs.com/%s/%sbackend?criteria=category%s%s%s%s";
// https://nofluffjobs.com/pl/warszawa/backend?criteria=category%3Dtesting%20seniority%3Dmid%20requirement%3DJava&page=1

    protected Document getDocument(String codeISO, String workplace, String page, String level, String language) {
        String city = getCityByCodeISO(codeISO, workplace).toLowerCase();
        return DocumentUtil.getDocument(format(url,
                isMatch(of("all", "remote"), codeISO) ? "ua" : codeISO.equals("foreign") ? "pl" : codeISO,
                isMatch(remoteAria, workplace) ? getNoff("ua") : isMatches(of(foreignAria, of("all")), workplace) ?
                        "" : getJoin(city.equals("kyiv") ? "kiev" : city, isEmpty(city) ? "" : "/"),
                part,
                getLevel(nofluff, level), language.equals("all") ? "" : getJoin(part_language, language),
                getPage(nofluff, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        language = language.equals("ruby on rails") ? "%27Ruby%20on%20Rails%27" : getUpperStart(language);
        log.info(get_vacancy, workplace, language);
        String codeISO = getCodeISOByCity(workplace);
        if (!isMatch(List.of("pl", "ua", "cz", "sk", "hu", "remote", "foreign", "all"), codeISO)) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(codeISO, workplace, valueOf(page), level, language);
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

    public static String getNoff(String country) {
        return switch (country) {
            case "ua" -> "viddalena-robota/";
            case "pl" -> "praca-zdalna/";
            case "cz" -> "prace-na-dalku/";
            case "sk" -> "praca-na-dialku/";
            case "hu" -> "tavmunka/";
            case "de" -> "Homeoffice/";
            case "bg" -> "Дистанционната%20работа/";
            default -> "Remote"; // ca, uk
        };
    }
}
