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
    public static final int maxPages = 3;
    private static final String URL_FORMAT = "https://ua.indeed.com/jobs?q=%s+%s&l=Украина&rbl=%s&jlid=e9ab1a23f8e591f1&limit=50&sort=date&fromage=7&%s";
    //    https://ua.indeed.com/jobs?q=%s%s&l=Украина&rbl=%s&jlid=e9ab1a23f8e591f1&limit=50&sort=date&fromage=7&%s

    protected Document getDocument(String city, String language, String level, int page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, language, level.equals("trainee") ? "intern" : level,
                city, page == 0 ? "" : "start=".concat(String.valueOf(page*50))));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (!isMatchesRu(freshen.getWorkplace()) || freshen.getWorkplace().equals("foreign")) {
            return new ArrayList<>();
        }
        int page = 0;
        while (true) {
            Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage(), freshen.getLevel(), page);
            Elements elements = doc == null ? null : doc.getElementsByAttributeValueStarting("id","job_");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesIndeed(elements, freshen));
            if(page < Math.min(limitCallPages, maxPages)) page++;
            else break;
        }
        reCall(set.size(), new UAIndeedStrategy());
       return new ArrayList<>(set);
    }
}
