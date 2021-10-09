package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;
import ua.training.top.util.parser.data.CorrectAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesLinkedin;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelLinkedin;

public class LinkedinStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(LinkedinStrategy.class);
    public static final int maxPages = 10;
// https://www.linkedin.com/jobs/search?keywords=Java&location=Киев%2C%20Киев%2C%20Украина&locationId=&geoId=104035893&sortBy=DD&f_TPR=r604800&distance=25&f_E=4&position=1&pageNum=0
    // ->
// https://www.linkedin.com/jobs/search?keywords=Java&geoId=104035893&sortBy=DD&f_TPR=r604800&distance=25&f_E=4&position=1&pageNum=0
    private static final String URL_FORMAT = "https://www.linkedin.com/jobs/search?keywords=%s&geoId=%s&sortBy=DD&f_TPR=r604800&distance=25&f_E=%s&position=1&pageNum=%s";

    protected Document getDocument(String city, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city, getLevelLinkedin(level), page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String[] cityOrCountry = freshen.getWorkplace().equals("foreign") ? getForeign() : new String[]{freshen.getWorkplace()};
        List<VacancyTo> result = new ArrayList<>();
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (freshen.getWorkplace().equals("remote")){
            return new ArrayList<>();
        }
            for(String location : cityOrCountry) {
            int page = 0;
            while(page < maxPages) {
                Document doc = getDocument(CorrectAddress.getCityLinkedin(location), freshen.getLanguage(),
                        freshen.getLevel(), String.valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByAttributeValueStarting("class","base-card base-card--link base-search-card base-search-card--link");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesLinkedin(elements, freshen));
                page = cityOrCountry.length == 1 ? page + 1 : page + maxPages;
            }
        }
        result.addAll(set);
        reCall(result.size(), new LinkedinStrategy());
        return result;
    }

    public static final  String[] getForeign() {
        return new String[]{"канада", "польша", "германия", "швеция", "израиль", "швейцария", "сша", "франция", "италия",
                "финляндия", "сингапур", "англия", "эмираты", "чехия"};
    }
}
