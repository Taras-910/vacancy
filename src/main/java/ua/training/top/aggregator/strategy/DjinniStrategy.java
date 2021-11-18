package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.collect.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesDjinni;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;

public class DjinniStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);
    private static final String url = "https://djinni.co/jobs/keyword-%s/%s%s%s%s%s%s";
    //    https://djinni.co/jobs/keyword-java/?exp_level=1y&keywords=%28other%29&full_text=on&page=2

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url,
                language, workplace.equals("all") && level.equals("all") && page.equals("1") ? "" : "?",
                getLevel(djinni, level), level.equals("all") || workplace.equals("all") && page.equals("1") ? "" : "&",
                getWrokplaceDjinni(workplace),
                level.equals("all") && workplace.equals("all") || page.equals("1") ? "" : "&",
                getPage(djinni, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        workplace = freshen.getWorkplace().equals("foreign") ? "other" : workplace;
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (isCityRu(workplace)) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("list-jobs__item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesDjinni(elements, freshen));
            if (page < getMaxPages(djinni, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<>(set);
    }

    public static String getWrokplaceDjinni(String workplace) {
        return workplace.equals("all") ? "" : "keywords=%28".concat(workplace).concat("%29&full_text=on");
    }
}
