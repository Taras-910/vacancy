package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.to.VacancyTo;

import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.installation.InstallationUtil.limitVacanciesToKeep;
import static ua.training.top.aggregator.installation.InstallationUtil.reasonPeriodToKeep;
import static ua.training.top.model.Goal.FILTER;
import static ua.training.top.model.Goal.UPGRADE;

public class FreshenUtil {
    public static Logger log = LoggerFactory.getLogger(FreshenUtil.class) ;
    public static final String FRESHEN_NOT_BE_NULL = "freshen must not be null";

    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, now(), vTo.getLanguage(), vTo.getLevel(),
                hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), singleton(UPGRADE), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), now(), f.getLanguage(), f.getLevel(), f.getWorkplace(),
                f.getGoals() == null ? singleton(UPGRADE) : f.getGoals(), authUserId());
    }

    public static Freshen asNewFreshen(String language, String level, String workplace, Goal goal){
        return new Freshen(null, now(), language, level, workplace, singleton(goal == null ? UPGRADE : goal), authUserId());
    }

    public static List<Freshen> getFreshensOutPeriodToKeep(List<Freshen> freshensDb) {
        return freshensDb.stream()
                .filter(freshen -> reasonPeriodToKeep.isAfter(freshen.getRecordedDate().toLocalDate()))
                .collect(Collectors.toList());
    }

    public static List<Freshen> getFreshensOutLimitHeroku(List<Freshen> freshensDb) {
        List<Freshen> freshensFilter = freshensDb.stream()
                .filter(f -> f.getGoals().contains(FILTER))
                .collect(Collectors.toList());
        return freshensFilter.stream()
                .sorted((f1, f2) -> f1.getRecordedDate().isBefore(f2.getRecordedDate()) ? 1 : 0)
                .skip(Math.max(limitVacanciesToKeep / 2 + freshensFilter.size() - freshensDb.size(), 0))
                .collect(Collectors.toList());
    }
}
