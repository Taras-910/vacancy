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
import java.util.regex.Matcher;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.service.AggregatorService.herokuRestriction;
import static ua.training.top.util.collect.ElementUtil.getVacanciesLinkedin;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PatternUtil.pattern_salaries_linkedin;
import static ua.training.top.util.collect.data.SalaryUtil.getCurrencyCode;
import static ua.training.top.util.collect.data.SalaryUtil.getRemovedZeroPart;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.WorkplaceUtil.getLinkedin;

public class LinkedinStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(LinkedinStrategy.class);
    private static final String url = "https://www.linkedin.com/jobs/search?keywords=%s%s&f_TPR=%s&%sposition=1&pageNum=%s";
// https://www.linkedin.com/jobs/search?keywords=Java&geoId=104035893&sortBy=DD&f_TPR=r604800&distance=25&f_E=4&position=1&pageNum=0

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, language, getLinkedin(workplace),
                workplace.equals("remote") ? "" : "&f_WT=2", getLevel(linkedin, level), page));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        if (workplace.equals("россия")) {
            return new ArrayList<>();
        }
        String[] cityOrCountry = workplace.equals("all") ? new String[]{"украина"} : new String[]{workplace};

        if(herokuRestriction) {
        cityOrCountry = workplace.equals("foreign") ? getForeign() :
                workplace.equals("canada") || workplace.equals("канада") ? getCanada() :
                        workplace.equals("украина") || workplace.equals("all") ? getUA() : new String[]{workplace};
        }

        Set<VacancyTo> set = new LinkedHashSet<>();
            for(String location : cityOrCountry) {
            int page = 0;
            while(true) {
                Document doc = getDocument(location, language, level, valueOf(page));
                Elements elements = doc == null ? null : doc.getElementsByTag("li");
                if (elements == null || elements.size() == 0) break;
                set.addAll(getVacanciesLinkedin(elements, freshen));
                if (page < getMaxPages(linkedin, freshen.getWorkplace())) page++;
                else break;
            }
        }
        List<VacancyTo> result = new ArrayList<>(set);
        reCall(result.size(), new LinkedinStrategy());
        return result;
    }

    public static String getToLinkedin(String address) {
        String[] addressParts = address.split(",");
        return addressParts.length > 1 && addressParts[0].trim().equalsIgnoreCase(addressParts[1].trim()) ?
                address.substring(address.indexOf(",") + 1).trim() : address;
    }

    public static String[] getForeign() {
        return new String[]{"канада", "польша", "германия", "швеция", "израиль", "швейцария", "сша", "франция", "италия",
                "финляндия", "сингапур", "англия", "оаэ", "чехия", "черногория"};
    }

    public static String[] getUA() {
        return new String[]{"украина", "киев", "харьков", "львов", "одесса", "днепр", "винница", "ужгород",
                "ивано-франковск", "полтава", "запорожье", "черкассы", "чернигов", "тернополь"};
    }

    public static String[] getCanada() {
        return new String[]{"канада", "торонто", "ванкувер", "монреаль", "квебек", "онтарио", "брамптон", "виктория",
                "оттава", "гамильтон", "виннипег"};
    }

    public static String getSalaryLinkedin(String title) {
        String text = getRemovedZeroPart(title).toLowerCase();
        String code = getCurrencyCode(text);
        Matcher m = pattern_salaries_linkedin.matcher(text);
        String result = code;
        while(m.find()){
            result = getJoin(result, m.group().replaceAll("[^\\d-k\\s]", "").replaceAll("k","000"));
        }
        result = result.indexOf("-") != -1 ? result.replace("-", getJoin(code.equals("₭")?"000000 -":" -", code)) : getJoin(code,result);
        return getJoin(result, title.indexOf("month") == -1 ? " year" : " month");
    }
}
