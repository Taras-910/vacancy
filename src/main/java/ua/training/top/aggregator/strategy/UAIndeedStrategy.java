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
import static ua.training.top.util.parser.ElementUtil.getVacanciesIndeed;
import static ua.training.top.util.parser.data.CorrectAddress.isMatchesRu;

public class UAIndeedStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAIndeedStrategy.class);
    private static final String URL_FORMAT = "https://ua.indeed.com/jobs?q=%s&l=%s&sort=date&fromage=7&start=%s";
    // за 7 дней https://ua.indeed.com/jobs?q=java&l=киев&sort=date&fromage=7&start=10
    // удаленно -- // --
    protected Document getDocument(String city, String language, String page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city, page.equals("0") ? "" : page.concat("0")));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (!isMatchesRu(freshen.getWorkplace()) || freshen.getWorkplace().equals("за_рубежем")) {
            return new ArrayList<>();
        }
        int page = 0;
        while (true) {
            Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("jobsearch-SerpJobCard unifiedRow row result");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesIndeed(elements, freshen));
            if(page < limitCallPages) page++;
            else break;
        }
        reCall(set.size(), new UAIndeedStrategy());
       return new ArrayList<VacancyTo>(set);
    }
}
