package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ua.training.top.aggregator.strategy.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getNofluffjobsVacancies;

public class NofluffjobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(NofluffjobsStrategy.class);
    private static final String URL_PART1 = "https://nofluffjobs.com/jobs/remote/backend?criteria=category%3Dtesting%20seniority%3Dmid%20";
                                  // pl  https://nofluffjobs.com/pl/jobs/remote/backend?lang=en&criteria=category%3Dtesting%20seniority%3Dmid%20java%20rest%20spring&page=1

    protected Document getDocument(String language, String page) {
        String URL = URL_PART1.concat(language).concat("%20rest%20spring");
        return DocumentUtil.getDocument(page.equals("1") ? URL : URL.concat("&page=").concat(page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(freshen.getLanguage(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("posting-list-item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getNofluffjobsVacancies(elements));
            if(page < limitCallPages) page++;
            else break;
        }
        reCall(set.size(), new NofluffjobsStrategy());
        return new ArrayList<>(set);
    }

    public static String validAddress(String address) {
        return address.equals("") ? "POL" : address;
    }

    public static LocalDate validDate(String text) {
        return text.contains("NEW") || text.contains("NOVĚ")? LocalDate.now() : LocalDate.now().minusDays(7);
    }
}
