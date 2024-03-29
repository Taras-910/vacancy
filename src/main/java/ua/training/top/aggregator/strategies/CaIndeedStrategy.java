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
import static java.lang.String.valueOf;
import static java.util.List.of;
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.MessageUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.isMatches;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getPage;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getCa;

public class CaIndeedStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(CaIndeedStrategy.class);
    private static final String url = "https://ca.indeed.com/jobs?q=%s%s%s%s";
    //https://ca.indeed.com/jobs?q=java+middle&l=Ontario&start=10

    protected Document getDocument(String workplace, String language, String level, String page) {
        return DocumentUtil.getDocument(format(url, language, level.equals(all) ? "" : getJoin("+",level),
                getJoin("&l=",workplace), getPage(indeed_ca, page)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = getCa(freshen.getWorkplace()), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        Set<VacancyTo> set = new LinkedHashSet<>();
        if (!isMatches(of(caAria, citiesCa), workplace)) {
            return new ArrayList<>();
        }
        int page = 0;
        while (true) {
            Document doc = getDocument(workplace, language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByTag("li");
            if (elements == null || elements.size() == 0) break;
//            set.addAll(getVacanciesCaIndeed(elements, freshen));
            if (page < getMaxPages(indeed, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new UAIndeedStrategy());
        return new ArrayList<>(set);
    }
}
