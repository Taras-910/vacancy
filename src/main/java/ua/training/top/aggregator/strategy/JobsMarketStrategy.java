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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesJobsMarket;
import static ua.training.top.util.collect.data.DataUtil.get_vacancy;
import static ua.training.top.util.collect.data.DataUtil.jobsmarket;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;

public class JobsMarketStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsMarketStrategy.class);
    private static final String url = "https://jobsmarket.io/search?position=%s&page=%s";
    //    https://jobsmarket.io/search?position=Java%20Developer&page=2

    protected Document getDocument(String position, String page) {
        return DocumentUtil.getDocument(format(url, position, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (!isWorkplaceJobsMarket(workplace)) {
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
                Elements elements = doc == null ? null : doc.getElementsByAttributeValue("class", "card");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesJobsMarket(elements, freshen));
                if (page < getMaxPages(jobsmarket, position)) page++;
                else break;
            }
        }
        reCall(set.size(), new JobsMarketStrategy());
        return new ArrayList<>(set);
    }

    public static boolean isWorkplaceJobsMarket(String workplace) {
        return workplace.equals("all") || workplace.equals("foreign") || workplace.equals("remote");
    }
}
