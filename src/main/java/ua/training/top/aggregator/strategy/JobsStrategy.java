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
import static ua.training.top.util.parser.ElementUtil.getVacanciesJobs;
import static ua.training.top.util.parser.data.CorrectAddress.isMatchesWorkplaceRabotaIndeedJobs;

public class JobsStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsStrategy.class);
    private static final String URL_FORMAT = "https://jobs.dou.ua/vacancies/?category=%s&%s";
    // Киев        https://jobs.dou.ua/vacancies/?category=Java&city=Киев
    // за рубежем  https://jobs.dou.ua/vacancies/?category=Java&relocation=
    // удаленно    https://jobs.dou.ua/vacancies/?category=Java&remote

    protected Document getDocument(String city, String language) {
        city = city.equals("за_рубежем") ? "relocation=" : city.equals("удаленно") ? "remote" : "city=".concat(city);
        return DocumentUtil.getDocument(format(URL_FORMAT, language, city));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        log.info("getVacancies city {} language {}", freshen.getWorkplace(), freshen.getLanguage());
        if (!isMatchesWorkplaceRabotaIndeedJobs(freshen.getWorkplace())) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        Document doc = getDocument(freshen.getWorkplace(), freshen.getLanguage());
        Elements elements = doc == null ? null : doc.getElementsByClass("vacancy");
        set.addAll(getVacanciesJobs(elements, freshen));
        reCall(set.size(), new JobsStrategy());
        return new ArrayList<>(set);
    }
}
