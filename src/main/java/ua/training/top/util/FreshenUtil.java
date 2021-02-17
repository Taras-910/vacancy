package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Collections.singleton;
import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.model.Goal.UPGRADE;

public class FreshenUtil {
    public static final String FRESHEN_NOT_BE_NULL = "freshen must not be null";

    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, LocalDateTime.now(), vTo.getLanguage(),
                hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), singleton(Goal.UPGRADE), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), f.getRecordedDate() == null ? LocalDateTime.now() : f.getRecordedDate(),
                f.getLanguage(), f.getWorkplace(), f.getGoals() == null ? singleton(Goal.UPGRADE) : f.getGoals(), authUserId());
    }
    public static Freshen asNewFreshen(String language, String workplace, Goal goal){
        return new Freshen(null, LocalDateTime.now(), language, workplace,
                singleton(goal == null ? Goal.UPGRADE : goal), authUserId());
    }

    public static  Freshen schedulingFreshen(String workplace) {
        return new Freshen(null, LocalDateTime.now(), "java", workplace, Collections.singleton(UPGRADE), 100000);
    }
}
