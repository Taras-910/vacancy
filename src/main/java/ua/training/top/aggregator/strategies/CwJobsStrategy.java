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
import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getPage;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getUK;

public class CwJobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(CwJobsStrategy.class);
    private final static String url_part = "facet_selected%3bcompanyTypes%3b2",
            url = "https://www.cwjobs.co.uk/jobs%s%s%s?%saction=%s&companytypes=2&postedWithin=14";
//https://www.cwjobs.co.uk/jobs/middle-java/in-london?page=2&action=facet_selected%3bcompanyTypes%3b2&companytypes=2&postedWithin=14

    protected Document getDocument(String workplace, String page, String level, String language) {
        return DocumentUtil.getDocument(format(url, language.equals("all") ?  "" : getJoin("/", language),
                level.equals("all") ? "" : getJoin("/", level),
                workplace.equals("all") ? "" : getJoin("/in-", workplace), getPage(cwjobs, page), url_part));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        workplace = isMatches(of(remoteAria, foreignAria, of("all")), workplace) ? "all" :
                isMatch(citiesUK, workplace)? getUK(workplace).toLowerCase() : "-1";
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByAttributeValue("data-at","job-item");
            if (elements == null || elements.size() == 0) break;
//            set.addAll(getVacanciesCwJobs(elements, freshen));
            if (page < getMaxPages(itJob, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new CwJobsStrategy());
        return new ArrayList<>(set);
    }
}
