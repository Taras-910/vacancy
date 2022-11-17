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
import static ua.training.top.util.collect.data.CommonUtil.*;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getUK;

public class ReedStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(ReedStrategy.class);
    private final static String url = "https://www.reed.co.uk/jobs/%s%sjobs%s?%sparentsector=it-telecoms&orgId=1&agency=True&direct=True&datecreatedoffset=LastTwoWeeks%s";
//https://www.reed.co.uk/jobs/java-jobs-in-london?pageno=2&parentsector=it-telecoms&orgId=1&agency=True&direct=True&datecreatedoffset=LastTwoWeeks&hideTrainingJobs=True

    protected Document getDocument(String workplace, String page, String level, String language) {
        return DocumentUtil.getDocument(format(url,
                language.equals("all") ? "" : getJoin(language,"-"),
                "", workplace.equals("all") ? "" : getJoin("-in-", workplace),
                getPage(reed, page),
                isMatch(traineeAria, level) ? "hideTrainingJobs=True" : ""));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        workplace = isMatches(of(ukAria, remoteAria, foreignAria), workplace) || workplace.equals("all") ? "all" :
                isMatch(citiesUK, workplace)? getUK(workplace).toLowerCase() : "-1";
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByAttributeValueEnding("class","details");
            if (elements == null || elements.size() == 0) break;
//            set.addAll(getVacanciesReed(elements, freshen));
            if (page < getMaxPages(itJob, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new ReedStrategy());
        return new ArrayList<>(set);
    }
}