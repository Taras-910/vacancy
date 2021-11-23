package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.collect.DocumentUtil;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.time.LocalDate.now;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesRabota;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    private static final String url = "https://rabota.ua/zapros/%s/%s%s?scheduleId=%s%s&agency=false&period=3&lastdate=%s";
//https://rabota.ua/zapros/%s/%s%s?scheduleId=%s%s&agency=false&period=3&lastdate=%s

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, language, getRabota(workplace), getPage(rabota, page),
                getUrlShed(workplace, level), getLevel(rabota, level), getUrlDate()));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (isMatch(citiesRU, workplace)) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while(true) {
            Document doc = getDocument(workplace, language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesRabota(elements, freshen));
            if (page < getMaxPages(rabota, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        return new ArrayList<>(set);
    }

    private Object getUrlShed(String workplace, String level) {
        return workplace.equals("remote") ? "3" : level.equals("trainee") ? "4" : "1";
    }

    public static String getUrlDate() {
        return now().minusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
