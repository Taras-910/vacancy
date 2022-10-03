package ua.training.top.aggregator.strategies;

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
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.collect.data.CommonUtil.getJoin;
import static ua.training.top.util.collect.data.ConstantsUtil.get_vacancy;
import static ua.training.top.util.collect.data.ConstantsUtil.indeed;
import static ua.training.top.util.collect.data.PageUtil.getMaxPages;
import static ua.training.top.util.collect.data.PageUtil.getPage;
import static ua.training.top.util.collect.data.WorkplaceUtil.getIndeed;

public class UAIndeedStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(UAIndeedStrategy.class);
    private static final String url = "https://ua.indeed.com/jobs?q=%s%s%s&fromage=14%s";
    //https://ua.indeed.com/jobs?q=java+middle&rbl=Киев&jlid=e9ab1a23f8e591f1&start=10

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, language, level.equals("all") ? "" :
                getJoin("+",level), workplace, getPage(indeed, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = getIndeed(freshen.getWorkplace()), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, freshen.getWorkplace(), language);
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (workplace.equals("-1")) {
            return new ArrayList<>();
        }
        int page = 0;
        while (true) {
            Document doc = getDocument(workplace, language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByAttributeValueStarting("id", "job_");
            if (elements == null || elements.size() == 0) break;
//            set.addAll(getVacanciesIndeed(elements, freshen));
            if (page < getMaxPages(indeed, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new UAIndeedStrategy());
        return new ArrayList<>(set);
    }
}
