package ua.training.top.aggregator.strategies;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.aggregatorUtil.DocumentUtil;

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
import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.aggregatorUtil.ElementUtil.getJobsBG;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.DateToUtil.defaultDate;
import static ua.training.top.util.aggregatorUtil.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.PatternUtil.pattern_date_jobs_bg;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getBG_en;

public class JobsBGStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsBGStrategy.class);
    private final static String
            part_url = "subm=1&categories%5B%5D=56&domains%5B%5D=2",
            url = "https://www.jobs.bg/front_job_search.php?%s%s%s%s&last=5";
//https://www.jobs.bg/front_job_search.php?subm=1&categories%5B%5D=56&domains%5B%5D=2&techs%5B%5D=Java&location_sid=1&last=5
//    https://www.jobs.bg/front_job_search.php?subm=1&categories%5B%5D=56&domains%5B%5D=2&techs%5B%5D=java&last=5
    protected Document getDocument(String workplace, String page, String level, String language) {
        String city = isMatch(citiesBg, workplace) ? getBG_en(workplace) : "";
        return DocumentUtil.getDocument(format(url, part_url,
                language.equals("all") || isMatch(foreignAria, workplace)? "" : getJoin("&techs%5B%5D=", language),
                workplace.equals("all") || isMatch(bgAria, workplace) ? "" : workplace.equals("remote") ? "&is_distance_job=1" : city,
                level.equals("all") ? "" : getJoin("&keywords%5B%5D=", level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        boolean bg = isMatches(of(bgAria, citiesBg, remoteAria, foreignAria, of("all")), workplace);
        if (!bg) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, valueOf(page), level, language);
            Elements elements = doc == null ? null : doc.getElementsByAttribute("additional-params");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getJobsBG(elements, freshen));
            if (page < getMaxPages(jabsBG, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new JobsBGStrategy());
        return new ArrayList<>(set);
    }

    public static String getAddress(String address) {
        address = address.replaceAll(filter_address_jobs_bg, "");
        return address;
    }

    public static LocalDate getDateJobsBG(String date) {
        Matcher m = pattern_date_jobs_bg.matcher(date);
        if(m.find()) {
            String[] parts = m.group().split("\\.");
            return parts.length != 3 ? defaultDate :
                    LocalDate.of(parseInt(getJoin("20",parts[2])), parseInt(parts[1]), parseInt(parts[0]));
        }
        return getToLocalDate(date);
    }
}
