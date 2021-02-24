package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.parser.DocumentUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static ua.training.top.aggregator.installation.InstallationUtil.limitCallPages;
import static ua.training.top.util.parser.ElementUtil.getVacanciesGrc;
import static ua.training.top.util.parser.data.CorrectAddress.getCodeGrc;

public class GrcStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(GrcStrategy.class);
    private static final String URL_FORMAT =         "https://grc.ua/search/vacancy?L_is_autosearch=false&area=%s&clusters=true&enable_snippets=true&order_by=publication_time&search_period=7&text=%s&page=%s";
    // киев за 7 дней  https://grc.ua/search/vacancy?L_is_autosearch=false&area=115&clusters=true&enable_snippets=true&order_by=publication_time&search_period=7&text=java&page=1
    private static final String URL_FORMAT_REMOTE = "https://grc.ua/search/vacancy?L_is_autosearch=false&clusters=true&enable_snippets=true&order_by=publication_time&schedule=remote&search_period=7&text=%s&page=%s";
    // удаленно https://grc.ua/search/vacancy?clusters=true&enable_snippets=true&order_by=publication_time&search_period=7&text=java&schedule=remote&from=cluster_schedule&showClusters=false
    private static final String URL_FORMAT_FOREIGN = "https://grc.ua/search/vacancy?clusters=true&enable_snippets=true&text=%s&L_save_area=true&area=%s&from=cluster_area&showClusters=true";
    //израиль        //https://grc.ua/search/vacancy?clusters=true&enable_snippets=true&text=java&L_save_area=true&area=33&from=cluster_area&showClusters=true
    //сша            //https://grc.ua/search/vacancy?clusters=true&enable_snippets=true&text=java&L_save_area=true&area=85&from=cluster_area&showClusters=true
    protected Document getDocument(String city, String language, String page) { //33 израиль 85 сша 27 германия 149 швеция 207 норвегия 74 польша
        if(city.equals("удаленная")){
            return DocumentUtil.getDocument(format(URL_FORMAT_FOREIGN, language, page.equals("0") ? "" : page));
        }
        boolean other = city.equals("33") || city.equals("85") || city.equals("27") || city.equals("149") || city.equals("207");
        return DocumentUtil.getDocument(other ? format(URL_FORMAT_FOREIGN, language, city) :
                format(URL_FORMAT, getCodeGrc(city), language, page.equals("0") ? "" : page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city={} language={}", freshen.getWorkplace(), freshen.getLanguage());
        boolean other = freshen.getWorkplace().contains("за_рубежем");
        List<VacancyTo> list = other ? getOther(freshen) : getCity(freshen);
//        reCall(list.size(), new GrcStrategy());
        return list ;
    }

    private List<VacancyTo> getCity(Freshen doubleString){
        log.info("getCity {}", doubleString.getWorkplace());
        Set<VacancyTo> set = new HashSet<>();
        int page = 0;
        while (true){
            Document doc = getDocument(doubleString.getWorkplace(), doubleString.getLanguage(), String.valueOf(page));
            Elements elements = doc == null ? null : doc.select("[data-qa=vacancy-serp__vacancy]");
            if(elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesGrc(elements, doubleString));
            if(page < limitCallPages) page++;
            else break;
        }
        return set.stream().filter(v -> v.getAddress().toLowerCase().contains(doubleString.getWorkplace())).collect(Collectors.toList());
    }

    private List<VacancyTo> getOther(Freshen doubleString){
        log.info("getOtherCountryVacancy language={}", doubleString.getLanguage());
        Set<VacancyTo> set = new LinkedHashSet<>();
        String[] countries = new String[]{"33", "85", "27", "149", "207"};
        for(String country : countries) {
            Document doc = getDocument(country, doubleString.getLanguage(), null);
            Elements elements = doc == null ? null : doc.select("[data-qa=vacancy-serp__vacancy]");
            if(elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesGrc(elements, doubleString));
        }
        return new ArrayList<>(set);
    }
}
