package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesJobsMarket;

public class JobsMarketStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsMarketStrategy.class);
    private static final String URL_FORMAT = "https://jobsmarket.io/search?position=%s&page=%s";
      /*за_рубежем*/
//    https://jobsmarket.io/search?position=Java%20Developer&page=2
//    https://jobsmarket.io/search?position=Java%20Backend%20Engineer%20-&page=2
//    https://jobsmarket.io/search?position=Java%20API%20Developer&page=1
//    https://jobsmarket.io/search?position=Java%20Automation%20Engineer%2FSDET
//    Kotlin%20Developer
//    https://jobsmarket.io/search?position=Kotlin%2FReact%20Fullstack%20Developer%20(m%2Fw%2Fd)


    protected Document getDocument(String position, String page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, position, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies workplace {} language {}", freshen.getWorkplace(), freshen.getLanguage());
        if (isMatchesJobsMarket(freshen)) {
           return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        String[] positions = freshen.getLanguage().equals("java") ? new String[]{"Java%20Developer",
                "Java%20Backend%20Engineer%20-", "Java%20API%20Developer", "Java%20Automation%20Engineer%2FSDET"} :
                new String[]{"Kotlin%20Developer", "Kotlin%2FReact%20Fullstack%20Developer%20(m%2Fw%2Fd)"};
        log.info("language={} positions={}", freshen.getLanguage(), positions.length);
        for(String position : positions) {
            int page = 1;
            while (true) {
                Document doc = getDocument(position, String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByClass("card");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesJobsMarket(elements, position));
                if (page < limitCallPages) page++;
                else break;
            }
        }
        reCall(set.size(), new JobsMarketStrategy());
        return new ArrayList<>(set);
    }

    private boolean isMatchesJobsMarket(Freshen freshen) {
        if (!freshen.getLanguage().equals("java") && !freshen.getLanguage().equals("kotlin")) {
            return true;
        }
        return !freshen.getWorkplace().equals("за_рубежем") && !freshen.getWorkplace().equals("удаленно");
    }
}
