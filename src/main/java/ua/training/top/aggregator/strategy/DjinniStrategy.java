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
import static ua.training.top.util.parser.ElementUtil.getVacanciesDjinni;
import static ua.training.top.util.parser.data.CorrectAddress.getTranslated;

public class DjinniStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);
    public static final int maxPages = 5;
    private static final String URL_FORMAT = "https://djinni.co/jobs/keyword-%s/%s/%s";
    // киев        https://djinni.co/jobs/keyword-java/kyiv/?page=1
    //             https://djinni.co/jobs/keyword-java/kyiv/?page=2
    // за_рубежем  https://djinni.co/jobs/keyword-java/relocate/?page=2
    // intern      https://djinni.co/jobs/keyword-java/kyiv/?exp_level=no_exp

    protected Document getDocument(String city, String language, String page, String level) {
        page = page.equals("1") ? "" : "?page=".concat(page);
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city, level.equals("intern") ? "?exp_level=no_exp" : page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String city = freshen.getWorkplace().equals("за_рубежем") || freshen.getWorkplace().equals("удаленно") ?
                "relocate" : getTranslated(freshen.getWorkplace());
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (city.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(city, freshen.getLanguage(), String.valueOf(page), freshen.getLevel());
            Elements elements = doc == null ? null : doc.getElementsByClass("list-jobs__item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesDjinni(elements, freshen));
            if(page < Math.min(limitCallPages, maxPages)) page++;
            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<VacancyTo>(set);
    }
}
