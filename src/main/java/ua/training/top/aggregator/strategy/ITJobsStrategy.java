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
import static ua.training.top.util.collect.ElementUtil.getVacanciesITJob;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getITJobs;

public class ITJobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(ITJobsStrategy.class);
    private final static String url = "https://www.itjobs.ca/en/search-jobs/?search=1%s%s%s%s%s";
//https://www.itjobs.ca/en/search-jobs/?search=1&categories=155%2C168&keywords=java+middle&location=Toronto%2C+ON&location-id=481104&location-type=1&page=2

    protected Document getDocument(String workplace, String page, String level, String language) {
        return DocumentUtil.getDocument(format(url, !language.equals("all") || !level.equals("all") ? "&keywords=" :  "",
                language.equals("all") ? "" : language,
                level.equals("all") ? "" : language.equals("all") ? level : getJoin("+", level),
                workplace.equals("all") ? "" : workplace.equals("remote") ? "&teleworking=true" : workplace,
                getPage(itJob, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        workplace = isMatch(citiesCanada, remoteAria, foreignAria, workplace) || workplace.equals("all") ? workplace : "-1";
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(getITJobs(workplace), valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByClass("details-wrapper");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesITJob(elements, freshen));
            if (page < getMaxPages(itJob, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new ITJobsStrategy());
        return new ArrayList<>(set);
    }
}
