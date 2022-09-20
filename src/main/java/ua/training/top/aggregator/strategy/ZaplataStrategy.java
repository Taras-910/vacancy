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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesZaplata;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getZaplata;

public class ZaplataStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(ZaplataStrategy.class);
    private final static String
            part_url = "200%3B10000",
            url = "https://www.zaplata.bg/ru/software/%s%sfirma-organizacia/?q=%s&price=%s%s";
//https://www.zaplata.bg/ru/software/sofia/stazhanti-studenti/firma-organizacia/?q=java&price=200%3B10000&page=2
//https://www.zaplata.bg/ru/software/%s%sfirma-organizacia/?q=%s&price=%s%s
//
//workplace (  удаленно foreign all ? “”  :   workplace/)
//level (all ? “” :  trainee || junior ? stazhanti-studenti/ : middle ?  sluzhiteli-rabotnitsi/ : senior ? eksperti-spetsialisti/  expert ?  menidzhmant/ )
//language (all ? java :  language)
//part(    200%3B10000   )
// page (1: “”  2:  &page= + page)

    protected Document getDocument(String workplace, String level, String language, String page) {
        return DocumentUtil.getDocument(format(url, getZaplata(workplace), getLevel(zaplata, level),
                language.equals("all") ? "java" : language, part_url, getPage(zaplata, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        workplace = isMatch(citiesBg, remoteAria, foreignAria, workplace) || workplace.equals("all") ? workplace : "-1";
        if (workplace.equals("-1")) {
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

//    public static String getSkills(String employerName, String skills) {
//        skills = skills.indexOf("[") != -1 || skills.indexOf("]") != -1 ? skills.replaceAll("[\\[\\]]", "") : skills;
//        return employerName.indexOf(" ") != -1 ? skills.substring(0, skills.indexOf(employerName.split(" ")[0]) - 2) : skills;
//    }
//
//    public static String getAddress(String address) {
//        address = address.replaceAll("Месторабота: wifi ", "");
//        return address.indexOf(";") != -1 ? address.substring(0, address.indexOf(";")) : address;
//    }
//
//    public static LocalDate getDateJobsBG(String date) {
//        Matcher m = pattern_date_jobs_bg.matcher(date);
//        if(m.find()) {
//            String[] parts = m.group().split("\\.");
//            return LocalDate.of(parseInt(getJoin("20",parts[2])), parseInt(parts[1]), parseInt(parts[0]));
//        }
//        return defaultDate;
//    }
}
