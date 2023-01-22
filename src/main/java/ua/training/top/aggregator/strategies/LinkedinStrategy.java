package ua.training.top.aggregator.strategies;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.aggregatorUtil.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.service.AggregatorService.herokuRestriction;
import static ua.training.top.util.MessageUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.ElementUtil.getVacanciesLinkedin;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.isMatch;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.LevelUtil.getLevel;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getLinkedin;

public class LinkedinStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(LinkedinStrategy.class);
    private static final String url = "https://www.linkedin.com/jobs/search?keywords=%s%s&f_TPR=r86400%s&%sposition=1&pageNum=%s";
    // https://www.linkedin.com/jobs/search?keywords=%s%s&f_TPR=r86400&distance=25&f_E=4&position=1&pageNum=%s
    // https://www.linkedin.com/jobs/search?keywords=Java&geoId=104035893&sortBy=DD&f_TPR=r86400&distance=25&f_E=4&position=1&pageNum=0

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url,
                language.equals(all) ? "" : language,
                getLinkedin(workplace),
                workplace.equals("remote") ? "" : "&f_WT=2", getLevel(linkedin, level), page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        language = language.equals("ruby on rails") ? "Ruby%20on%20Rails" : language;
        log.info(get_vacancy, language, level, workplace);
        if (isMatch(citiesRU, workplace)) {
            return new ArrayList<>();
        }
        String[] cityOrCountry;
        if (!herokuRestriction) {
            cityOrCountry = isMatch(foreignAria, workplace) ? getForeign() : isMatch(caAria, workplace) ? getCanada() :
                    isMatch(uaAria, workplace) ? getUA() : new String[]{workplace};
        } else {
            cityOrCountry = workplace.equals(all) ? new String[]{"украина"} : new String[]{workplace};
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        for (String location : cityOrCountry) {
            int page = 0;
            while (true) {
                Document doc = getDocument(location, language, level, valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("base-search-card__info");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesLinkedin(elements, freshen));
                if (page < getMaxPages(linkedin, freshen.getWorkplace())) page++;
                else break;
            }
        }
        List<VacancyTo> result = new ArrayList<>(set);
        reCall(result.size(), new LinkedinStrategy());
        return result;
    }

    private static String[] getForeign() {
        return new String[]{"канада", "польша", "германия", "швеция", "израиль", "швейцария", "сша", "франция", "италия",
                "финляндия", "сингапур", "англия", "оаэ", "норвегія", "австралія", "филиппины", "греция", "кипр"};
    }

    private static String[] getUA() {
        return new String[]{"украина", "киев", "харьков", "львов", "одесса", "днепр", "винница", "ужгород", "полтава"};
    }

    private static String[] getCanada() {
        return new String[]{"канада", "торонто", "ванкувер", "онтарио", "брамптон", "виктория", "оттава", "гамильтон"};
    }
}
