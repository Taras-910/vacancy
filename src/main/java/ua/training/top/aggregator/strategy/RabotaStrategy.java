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
import static ua.training.top.util.parser.ElementUtil.getVacanciesRabota;
import static ua.training.top.util.parser.data.CorrectAddress.getCityRabota;
import static ua.training.top.util.parser.data.CorrectAddress.isMatchesRu;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelRabota;
import static ua.training.top.util.parser.date.DateUtil.dateRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    public static final int maxPages = 10;
    private static final String URL = "https://rabota.ua/zapros/%s/%s%s?scheduleId=%s&parentId=1&profLevelIDs=%s&agency=false&period=3&lastdate=%s";

    protected Document getDocument(String city, String language, String level, String page) {
        return DocumentUtil.getDocument(format(URL, language, getCityRabota(city),
                page.equals("1") ? "" : "/pg".concat(page), getSheduled(city, level), getLevelRabota(level), dateRabota()));
    }

    private Object getSheduled(String workplace, String level) {
        return workplace.equals("удаленно") ? "3" : level.equals("intern") ? "4" : "1";
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String city = freshen.getWorkplace();
        if (!isMatchesRu(city)) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while(true) {
            Document doc = getDocument(city, freshen.getLanguage(), freshen.getLevel(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesRabota(elements, freshen));
            if(page < Math.min(limitCallPages, maxPages) && !city.equals("за_рубежем")) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        return new ArrayList<>(set);
    }
}
