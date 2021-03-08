package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getVacanciesRabota;
import static ua.training.top.util.parser.data.CorrectAddress.isMatchesWorkplaceRabotaIndeedJobs;
import static ua.training.top.util.parser.date.DateUtil.printStrategyRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    private static final String URL_FORMAT = "https://rabota.ua/zapros/%s/%sprofLevelIDs=3&agency=false&period=3&lastdate=";
    // другие страны   стр1      https://rabota.ua/zapros/java/другие_страны?profLevelIDs=3&agency=false&period=3&lastdate=01.03.2021
    // удаленно        стр1      https://rabota.ua/zapros/java/украина?scheduleId=3&profLevelIDs=3&agency=false&period=3&lastdate=01.03.2021
    // удаленно        стр2      https://rabota.ua/zapros/java/украина/pg2?scheduleId=3&profLevelIDs=3&agency=false&period=3&lastdate=01.03.2021
    // киев            стр1      https://rabota.ua/zapros/java/киев?scheduleId=3&   profLevelIDs=3&agency=false&period=3&lastdate=01.03.2021
    // киев            стр2      https://rabota.ua/zapros/java/киев/pg2?scheduleId=3&profLevelIDs=3&agency=false&period=3&lastdate=01.03.2021
    protected Document getDocument(String city, String language, String page) {
        page = page.equals("1") ? "" : "/pg".concat(page);
        if(city.equals("за_рубежем")) {
            city = "другие_страны?";
        } else {
            city = city.equals("удаленно") ? "украина".concat(page).concat("?scheduleId=3&") : city.concat(page).concat("?scheduleId=3&");
        }
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city).concat(printStrategyRabota(LocalDate.now().minusDays(7))));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        String city = freshen.getWorkplace();
        if (!isMatchesWorkplaceRabotaIndeedJobs(city)) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while(true) {
            Document doc = getDocument(city, freshen.getLanguage(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesRabota(elements, freshen));
            if(page < limitCallPages && !city.equals("за_рубежем")) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        return new ArrayList<>(set);
    }
}
