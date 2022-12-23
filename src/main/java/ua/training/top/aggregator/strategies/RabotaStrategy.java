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
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.rabota;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getPage;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    private static final String url = "https://rabota.ua/ua/%s%s%s%s%s%s%s%s";

    protected Document getDocument(String workplace, String language, String level, String page) {
        return  DocumentUtil.getDocument(format(url, workplace.equals("украина") || workplace.equals("киев") ? "zapros/" : "",
                language.equals("all") ? "" : language,
                level.equals("all") ? "" : language.equals("all") ? level : getJoin("-", level),
                workplace.equals("remote") ? level.equals("all") && language.equals("all") ? "віддалено" : "-віддалено" : "",
                language.equals("all") && level.equals("all") && !workplace.equals("remote")  ?  "" : "/",
                workplace.equals("украина") && level.equals("all") &&  language.equals("all") ? "all/"  : "",
                workplace, getPage(rabota, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while(true) {
            Document doc = getDocument(getRabota(workplace), language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card");
            if (elements == null || elements.size() == 0) break;
//            set.addAll(getVacanciesRabota(elements, freshen));
            if (page < getMaxPages(rabota, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        return new ArrayList<>(set);
    }
}
