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
import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesWork;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.LevelUtil.getLevel;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getUA_en;

public class WorkStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(WorkStrategy.class);
    private static final String url = "https://www.work.ua/ru/jobs%s-%s%s/?advs=1%s&notitle=1&days=123%s";
    //за 7 дней сорт по дате   https://www.work.ua/ru/jobs-kyiv-java/?days=123&page=1
//https://www.work.ua/ru/jobs%s-%s%s/?advs=1%s&notitle=1&days=123%s

    protected Document getDocument(String workspace, String language, String level, String page) {
        workspace = isMatch(uaAria, remoteAria, of("all"), workspace) ? "" : isMatch(citiesUA, workspace) ?
                getJoin("-", getUA_en(workspace).toLowerCase()) : "-other";
        return DocumentUtil.getDocument(format(url, workspace, language, getLevel(work, level),
                workspace.equals("remote") ? "&employment=76" : "", getPage(work, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (isMatch(citiesRU, workplace)) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card card-hover card-visited wordwrap job-link");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesWork(elements, freshen));
            if (page < getMaxPages(work, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new WorkStrategy());
        return new ArrayList<>(set);
    }

    public static String getAddrWork(String address) {
        return address.indexOf("VIP · ") > -1 ? address.replaceAll("VIP · ", "") : address;
    }
}
