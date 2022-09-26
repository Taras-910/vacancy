package ua.training.top.aggregator.strategy;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.collect.DocumentUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesJobsBG;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.DateToUtil.defaultDate;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PatternUtil.pattern_date_jobs_bg;
import static ua.training.top.util.collect.data.WorkplaceUtil.getBG_en;

public class JobsBGStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsBGStrategy.class);
    private final static String
            part_url = "subm=1&categories%5B%5D=56&domains%5B%5D=2",
            url = "https://www.jobs.bg/front_job_search.php?%s%s%s%s&last=6";
//https://www.jobs.bg/front_job_search.php?subm=1&categories%5B%5D=56&domains%5B%5D=2&techs%5B%5D=Java&location_sid=6&keywords%5B%5D=middle&last=6

    protected Document getDocument(String workplace, String page, String level, String language) {
        String city = isMatch(citiesBg, workplace) ? getBG_en(workplace) : "";
        return DocumentUtil.getDocument(format(url, part_url,
                language.equals("all") || isMatch(foreignAria, workplace)? "" : getJoin("&techs%5B%5D=", language),
                workplace.equals("all") || isMatch(bgAria, workplace)? "" : workplace.equals("remote") ? "&is_distance_job=1" : city,
                level.equals("all") ? "" : getJoin("&keywords%5B%5D=", level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        boolean bg = isMatch(bgAria, citiesBg, workplace) || isMatch(remoteAria, foreignAria, workplace) || workplace.equals("all");
        if (!bg) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByTag("li");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesJobsBG(elements, freshen));
            if (page < getMaxPages(jabsBG, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new JobsBGStrategy());
        return new ArrayList<>(set);
    }

    public static String getSkills(String employerName, String skills) {
        skills = skills.indexOf("[") != -1 || skills.indexOf("]") != -1 ? skills.replaceAll("[\\[\\]]", "") : skills;
        return employerName.indexOf(" ") != -1 ? skills.substring(0, skills.indexOf(employerName.split(" ")[0]) - 2) : skills;
    }

    public static String getAddress(String address) {
        address = address.replaceAll("Месторабота: wifi ", "");
        return address.indexOf(";") != -1 ? address.substring(0, address.indexOf(";")) : address;
    }

    public static LocalDate getDateJobsBG(String date) {
        Matcher m = pattern_date_jobs_bg.matcher(date);
        if(m.find()) {
            String[] parts = m.group().split("\\.");
            return LocalDate.of(parseInt(getJoin("20",parts[2])), parseInt(parts[1]), parseInt(parts[0]));
        }
        return defaultDate;
    }
}
