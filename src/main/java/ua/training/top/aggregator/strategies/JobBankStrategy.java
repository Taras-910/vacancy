package ua.training.top.aggregator.strategies;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;
import ua.training.top.util.aggregatorUtil.DocumentUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.MessageUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.ElementUtil.getJobBank;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.isMatches;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getCa;

public class JobBankStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobBankStrategy.class);
    private static final String url = "https://www.jobbank.gc.ca/jobsearch/jobsearch?searchstring=%s&locationstring=%s";
    //  https://www.jobbank.gc.ca/jobsearch/jobsearch?searchstring=java&locationstring=Toronto%2C+ON

    protected Document getDocument(String workplace, String language, String level) {
        workplace = isMatches(of(citiesCa, remoteAria), workplace) ? getCa(workplace) : "Canada";
        return DocumentUtil.getDocument(format(url, language.equals(all) ? "java" : language, workplace));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        language = language.equals("ruby on rails") ? "Ruby+on+Rails" : language;
        log.info(get_vacancy, language, level, workplace);
        boolean ca = isMatches(of(caAria, citiesCa, foreignAria, remoteAria, of(all)), workplace);
        if (!ca || language.equals("all")) {
            return new ArrayList<>();
        }
        Set<VacancyTo> set = new LinkedHashSet<>();
        Document doc = getDocument(workplace, language, level);
        Elements elements = doc == null ? null : doc.getElementsByClass("resultJobItem");
        if (doc == null || elements == null) return new ArrayList<>();
        set.addAll(getJobBank(elements, freshen));

        reCall(set.size(), new JobBankStrategy());
        return new ArrayList<>(set);
    }
}
