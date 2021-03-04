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
    private static final String URL_FORMAT = "https://djinni.co/jobs/keyword-%s/%s/%s";
    //             https://djinni.co/jobs/keyword-java/kyiv/?page=1
    // за_рубежем  https://djinni.co/jobs/keyword-java/relocate/?page=2

    protected Document getDocument(String city, String language, String page) {
        page = page.equals("1") ? "" : "page=".concat(page);
        city = city.equals("за_рубежем") ? "relocate" : getTranslated(city);
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city, page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String city = freshen.getWorkplace().equals("за_рубежем") ? "relocate" : getTranslated(freshen.getWorkplace());
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (city.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(city, freshen.getLanguage(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("list-jobs__item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesDjinni(elements, freshen));
            if(page < limitCallPages) page++;
            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<VacancyTo>(set);
    }
}
