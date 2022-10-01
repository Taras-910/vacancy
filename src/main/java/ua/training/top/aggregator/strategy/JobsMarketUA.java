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
import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getJobsMarketUA;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.HelpUtil.isMatches;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;

public class JobsMarketUA implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsMarketUA.class);
    private static final String url = "https://jobsmarket.com.ua/search?position=%s&page=%s";
//    private static final String url = "https://jobsmarket.com.ua/search?position=%s&page=%s";
//    https://jobsmarket.com.ua/search?position=Java%20Developer&page=2

    protected Document getDocument(String position, String page) {
        return DocumentUtil.getDocument(format(url, position, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (!isMatches(of(uaAria, remoteAria, of("all")), workplace)) {
           return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        String[] positions = freshen.getLanguage().equals("java") ? new String[]{"Java%20Developer",
                "Java%20Backend%20Engineer%20-", "Java%20API%20Developer", "Java%20Automation%20Engineer%2FSDET"} :
                new String[]{"Kotlin%20Developer", "Kotlin%2FReact%20Fullstack%20Developer%20(m%2Fw%2Fd)"};
        log.info("language={} positions={}", freshen.getLanguage(), positions);
        for(String position : positions) {
            int page = 1;
            while (true) {
                Document doc = getDocument(position, String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("job-description");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getJobsMarketUA(elements, freshen));
                if (page < getMaxPages(jobsmarket, position)) page++;
                else break;
            }
        }
        reCall(set.size(), new JobsMarketUA());
        return new ArrayList<>(set);
    }
}
