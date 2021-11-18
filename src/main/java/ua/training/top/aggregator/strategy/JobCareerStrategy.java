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
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getVacanciesJobCareer;
import static ua.training.top.util.collect.data.DataUtil.get_vacancy;
import static ua.training.top.util.collect.data.DataUtil.jobcareer;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.UrlUtil.getLevel;
import static ua.training.top.util.collect.data.UrlUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getCareer;

public class JobCareerStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobCareerStrategy.class);
    private static final String url = "https://%s.jobcareer.ru/jobs/%s%s%s/?feed=%s";
//https://harkov.jobcareer.ru/jobs/java-middle-remote/?feed=&page=2

    protected Document getDocument(String workplace, String language, String page, String level) {
        return DocumentUtil.getDocument(format(url, workplace.equals("remote") ? "ukraine" : workplace, language,
                getLevel(jobcareer, level), workplace.equals("remote") ? "-remote" : "", getPage(jobcareer, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = getCareer(freshen.getWorkplace()), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, String.valueOf(page), level);
            Elements elements = doc == null ? null : doc.getElementsByAttribute("data-key");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesJobCareer(elements, freshen));
            if (page < getMaxPages(jobcareer, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new JobCareerStrategy());
        return new ArrayList<>(set);
    }

    public static String getCareerUrl(String urlString, Freshen freshen) {
        return "https://".concat(getCareer(freshen.getWorkplace())).concat(".jobcareer.ru").concat(urlString);
    }
}
