package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.util.jsoup.DocumentUtil;
import ua.training.top.to.VacancyNet;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ua.training.top.aggregator.util.DateUtil.printStrategyRabota;
import static ua.training.top.aggregator.util.ProviderUtil.limitCallPages;
import static ua.training.top.aggregator.util.ProviderUtil.reCall;
import static ua.training.top.aggregator.util.jsoup.ElementUtil.getVacanciesRabota;

public class RabotaStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(RabotaStrategy.class);
    private static final String URL_FORMAT = "https://rabota.ua/zapros/%s/%s/%s?profLevelIDs=3&agency=false";
    // другие страны за 30 дней  https://rabota.ua/zapros/java/другие_страны?profLevelIDs=3&agency=false
    // за 7 дней специалист  https://rabota.ua/zapros/java/киев/pg2?profLevelIDs=3&agency=false&period=3&lastdate=23.11.2020

    protected Document getDocument(String city, String language, String page) {
        boolean other = city.equals("за_рубежем");
        String url = String.format(URL_FORMAT, language, other ? "другие_страны" : city, page.equals("1") ? "" : "/pg".concat(page));
        return DocumentUtil.getDocument(other ? url : url.concat("&period=3&lastdate=").concat(printStrategyRabota(LocalDate.now().minusDays(7))));
    }

    @Override
    public List<VacancyNet> getVacancies(String city, String language) throws IOException {
        log.info("city={} language={}", city, language);
        Set<VacancyNet> set = new LinkedHashSet<>();
        int page = 1;
        while(true) {
            Document doc = getDocument(city, language, String.valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("card");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesRabota(elements));
            if(page < limitCallPages) page++;
            else break;
        }
        reCall(set.size(), new RabotaStrategy());
        set.stream().filter(vn -> vn.getTitle().toLowerCase().contains(language.toLowerCase())
                || vn.getSkills().toLowerCase().contains(language.toLowerCase()));
        return new ArrayList<>(set);
    }
}
