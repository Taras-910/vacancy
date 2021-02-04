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
import static ua.training.top.util.parser.datas.CorrectAddress.getTranslated;

public class DjinniStrategy implements Strategy{
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);
    private static final String URL_FORMAT = "https://djinni.co/jobs/keyword-%s/%s/?page=%s";
    private static final String URL_FORMAT_FOREIGN = "https://djinni.co/jobs/l-intl/%s/?page=%s";
    //             https://djinni.co/jobs/keyword-java/kyiv/?page=1
    // за_рубежем  https://djinni.co/jobs/l-intl/java/?page=1

    protected Document getDocument(String city, String language, String page) {
        return DocumentUtil.getDocument(city.equals("за_рубежем") ? format(URL_FORMAT_FOREIGN, language, page) :
                format(URL_FORMAT, language, getTranslated(city), page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage(), String.valueOf(page));
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
