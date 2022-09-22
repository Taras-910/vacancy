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
import static ua.training.top.util.collect.ElementUtil.getVacanciesITJobsWatch;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getItJobsWatch;

public class ItJobsWatchStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(ItJobsWatchStrategy.class);
    private final static String url = "https://www.itjobswatch.co.uk/search?%s%ss=date%s";
//https://www.itjobswatch.co.uk/search?q=java+middle&l=London&s=date&start=50

    protected Document getDocument(String workplace, String page, String level, String language) {
        return DocumentUtil.getDocument(format(url,
                language.equals("all") && level.equals("all") ? "" : getJoin("q=",
                        level.equals("all") ? language : language.equals("all") ? level : getJoin(language, "+", level), "&"),
                workplace.equals("all") ? "" : getJoin("l=", workplace, "&"),
                getPage(itJobsWatch, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        workplace = isMatch(citiesUK, remoteAria, foreignAria, workplace) || workplace.equals("all") ? workplace : "-1";
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(getItJobsWatch(workplace), valueOf(page * 50 - 50), level, language);
            Elements elements = doc == null ? null : doc.getElementsByClass("job");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesITJobsWatch(elements, freshen));
            if (page < getMaxPages(itJob, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new ItJobsWatchStrategy());
        return new ArrayList<>(set);
    }
}
