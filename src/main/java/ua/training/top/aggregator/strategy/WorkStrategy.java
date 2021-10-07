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
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesWork;
import static ua.training.top.util.parser.data.CorrectAddress.getCityWork;
import static ua.training.top.util.parser.data.CorrectAddress.getTranslated;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelWork;

public class WorkStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(WorkStrategy.class);
    public static final int maxPages = 6;
    private static final String URL = "https://www.work.ua/ru/jobs%s-%s/?advs=1%s%s&days=123%s";
    //за 7 дней сорт по дате   https://www.work.ua/ru/jobs-kyiv-java/?days=123&page=1
//    https://www.work.ua/ru/jobs%s-%s/?advs=1%s&employment=76&days=123%s

 //   workspace ( «-»city  удаленная(вся украина) || Украина: «»  удаленная: «» ), language,
    //   level(«»  intern: «&student=1»  junior: «&experience=1»), part («»  удаленная: &employment=76), page(«» «page=»page )

    protected Document getDocument(String workspace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL, getCityWork(workspace), language, getLevelWork(level),
                workspace.equals("remote") ? "&employment=76" : "", page.equals("1") ? "" : "&page=".concat(page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        String city = getTranslated(freshen.getWorkplace());
        if (city.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(city, freshen.getLanguage(), freshen.getLevel(), valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card card-hover card-visited wordwrap job-link");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesWork(elements, freshen));
            if (page < Math.min(limitCallPages, maxPages)) page++;
            else break;
        }
        reCall(set.size(), new WorkStrategy());
        return new ArrayList<>(set);
    }
}
/*    private static final String URL_FORMAT = "https://www.work.ua/ru/jobs-%s-%s/?days=123&page=%s";
    //за 7 дней сорт по дате   https://www.work.ua/ru/jobs-kyiv-java/?days=123&page=1

    protected Document getDocument(String city, String language, String page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, city, language, page));
    }
*/
