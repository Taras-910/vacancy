package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.util.jsoup.DocumentUtil;
import ua.training.top.to.VacancyNet;

import java.io.IOException;
import java.util.*;

import static java.lang.String.format;
import static ua.training.top.aggregator.util.ProviderUtil.limitCallPages;
import static ua.training.top.aggregator.util.ProviderUtil.reCall;
import static ua.training.top.aggregator.util.jsoup.ElementUtil.getVacanciesGrc;

public class GrcStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(GrcStrategy.class);
    private static final String URL_FORMAT = "https://grc.ua/search/vacancy?L_is_autosearch=false&area=%s&clusters=true&enable_snippets=true&order_by=publication_time&search_period=7&text=%s&page=%s";
    // киев за 7 дней  https://grc.ua/search/vacancy?L_is_autosearch=false&area=115&clusters=true&enable_snippets=true&order_by=publication_time&search_period=7&text=java&page=1

    private static final String URL_FORMAT_OTHER = "https://grc.ua/search/vacancy?clusters=true&enable_snippets=true&text=%s&L_save_area=true&area=%s&from=cluster_area&showClusters=true";
    //израиль        //https://grc.ua/search/vacancy?clusters=true&enable_snippets=true&text=java&L_save_area=true&area=33&from=cluster_area&showClusters=true

    protected Document getDocument(String city, String language, String page) { //33 израиль 85 сша 27 германия 149 швеция 207 норвегия
        boolean other = city.equals("33") || city.equals("85") || city.equals("27") || city.equals("149") || city.equals("207");
        switch (city){
            case "киев" : city = "115";
                break;
            case "санкт-петербург" : city = "2";
        }
        return DocumentUtil.getDocument(other ? format(URL_FORMAT_OTHER, language, city) : format(URL_FORMAT, city, language, page.equals("0") ? "" : page));
    }

    @Override
    public List<VacancyNet> getVacancies(String city, String language) throws IOException {
        log.info("getVacancies city={} language={}", city, language);
        boolean other = city.equalsIgnoreCase("за_рубежем");
        List<VacancyNet> list = other ? getOther(language) : getCity(city, language);
        reCall(list.size(), new GrcStrategy());
        return list ;
    }

    private List<VacancyNet> getCity(String city, String language){
        Set<VacancyNet> set = new HashSet<>();
        int page = 0;
        while (true){
            Document doc = getDocument(city, language, String.valueOf(page));
            Elements elements = doc == null ? null : doc.select("[data-qa=vacancy-serp__vacancy]");
            if(elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesGrc(elements, language));
            if(page < limitCallPages) page++;
            else break;
        }
        return new ArrayList<>(set);
    }

    private List<VacancyNet> getOther(String language){
        log.info("getOtherCountryVacancy language={}", language);
        Set<VacancyNet> set = new LinkedHashSet<>();
        String[] countries = new String[]{"33", "85", "27", "149", "207"};
        for(String country : countries) {
            Document doc = getDocument(country, language, null);
            Elements elements = doc == null ? null : doc.select("[data-qa=vacancy-serp__vacancy]");
            if(elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesGrc(elements, language));
        }
        return new ArrayList<>(set);
    }
}
