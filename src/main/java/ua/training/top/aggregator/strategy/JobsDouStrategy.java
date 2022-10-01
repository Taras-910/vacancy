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
import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.reCall;
import static ua.training.top.util.collect.ElementUtil.getJobsDou;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.HelpUtil.*;
import static ua.training.top.util.collect.data.LevelUtil.getLevel;
import static ua.training.top.util.collect.data.WorkplaceUtil.getJobsDouForeign;
import static ua.training.top.util.collect.data.WorkplaceUtil.getUA_ua;

public class JobsDouStrategy implements Strategy {
    private final static Logger log = LoggerFactory.getLogger(JobsDouStrategy.class);
    private static final String url_trainee = "https://jobs.dou.ua/first-job/?from=exp";
    private static final String url = "https://jobs.dou.ua/%s/%s%s%s%s%s";
    // https://jobs.dou.ua/vacancies/?remote&from=maybecategory=Java&descr=1&city=Софія&exp=1-3

    protected Document getDocument(String workplace, String language, String level) {
        return DocumentUtil.getDocument(isMatch(traineeAria, workplace) ? url_trainee : format(url,
                isMatch(foreignAria, workplace) ? "jobs" : "vacancies",
                workplace.equals("all") && language.equals("all") && level.equals("all") ?  "" : "?",
                isMatch(remoteAria, workplace) ? "remote&from=maybe" : "",
                language.equals("all") || isMatch(remoteAria, workplace) ? "" : getJoin("category=", language, "&descr=1"),
                workplace.equals("all") || isMatches(of(remoteAria, foreignAria), workplace) ? "" :
                        getJoin(level.equals("all") && language.equals("all") ? "" : "&","city=",
                                isMatch(citiesUA, workplace) ? getUA_ua(workplace) : getJobsDouForeign(workplace)),
                level.equals("all") || isMatch(remoteAria, workplace) ? "" : getLevel(jobs, level)));
    }

    @Override
    public List<VacancyTo> getVacancies(Freshen freshen) throws IOException {
        String workplace = freshen.getWorkplace(), level = freshen.getLevel(), language = freshen.getLanguage();
        log.info(get_vacancy, workplace, language);
        String[] cities = isMatch(uaAria, workplace) ? getUA() : isMatch(foreignAria, workplace) ? getForeign() : new String[]{workplace};
        Set<VacancyTo> set = new LinkedHashSet<>();
        int i = cities.length;
        for(String location : cities) {
            Document doc = getDocument(location, language, level);
            Elements elements = doc == null ? null : doc.getElementsByClass("vacancy");
            if (doc == null || elements == null) {
                if(i-- == 0) break;
                continue;
            }
            set.addAll(getJobsDou(elements, freshen));
        }
        reCall(set.size(), new JobsDouStrategy());
        return new ArrayList<>(set);
    }

    private static String[] getUA() {
        return new String[]{"киев", "харьков", "львов", "одесса", "днепр", "винница", "ужгород", "чернівці", "полтава",
                "рівне", "ивано-франковск", "хмельницький", "черкассы", "чернигов", "тернополь", "житомир", "луцьк"};
    }

    private static String[] getForeign() {
        return new String[]{"краків",  "варшава",  "вроцлав",  "гданськ", "софія", "лодзь", "лісабон", "прага", "лімасол",
                "тбілісі", "рига", "таллінн", "варна", "вільнюс", "познань", "берлін", "зелена гура"};
    }
}
