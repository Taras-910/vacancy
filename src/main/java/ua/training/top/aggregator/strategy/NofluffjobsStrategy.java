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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.parser.ElementUtil.getNofluffjobsVacancies;
import static ua.training.top.util.parser.data.CorrectAddress.getCityNofluff;
import static ua.training.top.util.parser.data.CorrectLevel.getLevelNofluff;
import static ua.training.top.util.parser.data.DataUtil.nofluff;
import static ua.training.top.util.parser.data.PagesUtil.getMaxPages;

public class NofluffjobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(NofluffjobsStrategy.class);
    public static final int maxPages = 5;
    private final static String part_1 = "%3Dtesting%20seniority%3D";
    private final static String part_2 = "%20requirement%3D";
    private final static String URL_FORMAT ="https://nofluffjobs.com/pl/praca-it/%sbackend?page=%s&criteria=category%s%s%s%s";
    //удаленно
//https://nofluffjobs.com/pl/praca-it/praca-zdalna/backend?page=1&criteria=category%3Dtesting%20seniority%3Dexpert%20requirement%3Djava

    protected Document getDocument(String city, String page, String level, String language) {
        city = level.equals("intern") ? "" : city;
        return DocumentUtil.getDocument(format(URL_FORMAT, city, page, part_1, getLevelNofluff(level), part_2, language));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = getCityNofluff(freshen.getWorkplace());
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, String.valueOf(page), freshen.getLevel(), freshen.getLanguage());
            Elements elements = doc == null ? null : doc.getElementsByClass("posting-list-item");
            System.out.println("elements="+elements.size());
            if (elements == null || elements.size() == 0) break;
            set.addAll(getNofluffjobsVacancies(elements, freshen));
            if(page < getMaxPages(nofluff, workplace)) page++;
            else break;
        }
        reCall(set.size(), new NofluffjobsStrategy());
        System.out.println("set from elements = "+set.size());
        return new ArrayList<>(set);
    }
}
