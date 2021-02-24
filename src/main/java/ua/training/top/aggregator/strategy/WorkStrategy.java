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
import static ua.training.top.util.parser.data.CorrectAddress.getTranslated;

public class WorkStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(WorkStrategy.class);
    private static final String URL_FORMAT = "https://www.work.ua/ru/jobs-%s-%s/?days=123&page=%s";
    //за 7 дней сорт по дате   https://www.work.ua/ru/jobs-kyiv-java/?days=123&page=1

    protected Document getDocument(String city, String language, String page) {
        return DocumentUtil.getDocument(format(URL_FORMAT, getTranslated(city), language, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 0;
        while (true) {
            Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage(), valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card card-hover card-visited wordwrap job-link");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesWork(elements, freshen));
            if (page < limitCallPages) page++;
            else break;
        }
        reCall(set.size(), new WorkStrategy());
        return new ArrayList<>(set);
    }
}
