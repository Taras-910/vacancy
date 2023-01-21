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
import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.MessageUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.ElementUtil.getJobsMarket;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;

public class JobsMarketStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsMarketStrategy.class);
    private static final String url = "https://%sjobsmarket.io/search?position=%s&page=%s";
    //    private static final String url = "https://jobsmarket.io/search?position=%s&page=%s";
    //    https://jobsmarket.io/search?position=Java%20Developer&page=2

    protected Document getDocument(String codeISO, String position, String page) {
        codeISO = isEmpty(codeISO) ? "" : getJoin(codeISO, ".");
        return DocumentUtil.getDocument(format(url, codeISO, position, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), language = freshen.getLanguage(), level = freshen.getLevel();
        log.info(get_vacancy, language, level, workplace);
        String codeISO = isMatches(of(usAria, citiesUS, of("remote", "foreign", all)), workplace ) ? "us" :
                isMatches(of(ukAria, citiesUK), workplace) ? "uk" : isMatches(of(deAria, citiesDe), workplace) ? "de" : "";
        if (!isMatches(of(usAria, citiesUS, ukAria, citiesUK, deAria, citiesDe, of("remote", "foreign", "all")), workplace)) {
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
                Document doc = getDocument(codeISO, position, String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("item-block");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getJobsMarket(elements, freshen));
                if (page < getMaxPages(jobsmarket, position)) page++;
                else break;
            }
        }
        reCall(set.size(), new JobsMarketStrategy());
        return new ArrayList<>(set);
    }

    public static String getDateJobsMarket(String date) {
        return isContains(date, "on:") ?  date.split("on:")[1] : date;
    }
}
// jobsMarket only: us  uk  de
