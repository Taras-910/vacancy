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
import static ua.training.top.aggregator.InstallationUtil.reCall;
import static ua.training.top.util.MessageUtil.get_vacancy;
import static ua.training.top.util.aggregatorUtil.ElementUtil.getVacanciesDjinni;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.getJoin;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.getUpperStart;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.all;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.djinni;
import static ua.training.top.util.aggregatorUtil.data.LevelUtil.getLevel;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getMaxPages;
import static ua.training.top.util.aggregatorUtil.data.PageUtil.getPage;
import static ua.training.top.util.aggregatorUtil.data.WorkplaceUtil.getDjinni;

public class DjinniStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(DjinniStrategy.class);

    private static final String url = "https://djinni.co/jobs/%s%s%s%s";
        // https://djinni.co/jobs/?primary_keyword=Java&region=UKR&location=kyiv&exp_level=2y&page=2
        // https://djinni.co/jobs/%s%s%s%s

    protected Document getDocument(String workplace, String language, String level, String page) {
            return DocumentUtil.getDocument(format(url,
                    language.equals(all) ? "" : getJoin("?primary_keyword=", language.equals("php") ? "PHP" : getUpperStart(language)),
                    workplace.equals(all) ? "" : getJoin(language.equals(all) ? "?" : "&" , workplace.equals("remote") ? "employment=remote" : getDjinni(workplace)),
                    level.equals(all) ? "" : getJoin(language.equals(all) && workplace.equals(all) ? "?" : "&",  getLevel(djinni, level)),
                    page.equals("1") ? "" : getJoin(language.equals(all)  && workplace.equals(all)  && level.equals(all) ? "?" : "&",  getPage(djinni, page))));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, language, level, workplace);
        Set<VacancyTo> set = new LinkedHashSet<>();
        int page = 1;
        while (true) {
            Document doc = getDocument(workplace, language, level, valueOf(page));
            Elements elements = doc == null ? null : doc.getElementsByClass("list-jobs__item");
            if (elements == null || elements.size() == 0) break;
            set.addAll(getVacanciesDjinni(elements, freshen));
            if (page < getMaxPages(djinni, freshen.getWorkplace())) page++;
            else break;
        }
        reCall(set.size(), new DjinniStrategy());
        return new ArrayList<>(set);
    }
}
