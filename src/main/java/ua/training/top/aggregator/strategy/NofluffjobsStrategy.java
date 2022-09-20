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
import static ua.training.top.util.collect.ElementUtil.getNofluffjobsVacancies;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.WorkplaceUtil.getNofluff;

public class NofluffjobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(NofluffjobsStrategy.class);
    private final static String part = "%3Dtesting", part_language = "%20requirement%3D",
            url = "https://nofluffjobs.com/pl/praca-it/%sbackend?page=%s&criteria=category%s%s%s";
//    https://nofluffjobs.com/pl/praca-it/praca-zdalna/backend?page=1&criteria=category%3Dtesting%20seniority%3Dexpert%20requirement%3Djava

    protected Document getDocument(String workplace, String page, String level, String language) {
        return DocumentUtil.getDocument(format(url, workplace, page, part, getLevel(nofluff, level),
                getJoin(part_language, language)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = getNofluff(freshen.getWorkplace()), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByClass("posting-list-item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getNofluffjobsVacancies(elements, freshen));
            if (page < getMaxPages(nofluff, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new NofluffjobsStrategy());
        return new ArrayList<>(set);
    }

    public static String getToNofluffAddress(String address) {
        address = address.equals("") ? "Польша" : address.replaceAll("Zdalna", "Remote");
        return address;
    }
}
