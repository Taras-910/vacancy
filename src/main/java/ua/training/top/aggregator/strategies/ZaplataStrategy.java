package ua.training.top.aggregator.strategies;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.aggregatorUtil.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.MessageUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.ElementUtil.getVacanciesZaplata;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.isMatch;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.isMatches;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.LevelUtil.getLevel;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getPage;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getBG_en;

public class ZaplataStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(ZaplataStrategy.class);
    private final static String
            part_url = "200%3B10000",
            url = "https://www.zaplata.bg/ru/software/%s%sfirma-organizacia/?q=%s&price=%s%s";
    //https://www.zaplata.bg/ru/software/sofia/stazhanti-studenti/firma-organizacia/?q=java&price=200%3B10000&page=2
    //https://www.zaplata.bg/ru/software/firma-organizacia/?q=java&price=200%3B100001
    protected Document getDocument(String workplace, String level, String language, String page) {
        String city = isMatch(citiesBg, workplace) ? getBG_en(workplace) : "";
        return DocumentUtil.getDocument(format(url, workplace.equals(all) ? "" : city, getLevel(zaplata, level),
                language.equals(all) ? "java" : language, part_url, getPage(zaplata, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        workplace = isMatches(of(bgAria, remoteAria, foreignAria), workplace) ? all : workplace;
        boolean bg = isMatch((citiesBg), workplace) || workplace.equals(all);
        if (!bg) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, level, language, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("listItem");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesZaplata(elements, freshen));
            if (page < getMaxPages(zaplata, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new ZaplataStrategy());
        return new ArrayList<>(set);
    }
}
