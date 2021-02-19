package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.to.VacancyTo;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.UserUtil.ADMIN_ID;

public class FreshenUtil {
    public static final String FRESHEN_NOT_BE_NULL = "freshen must not be null";

    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, now(), vTo.getLanguage(),
                hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), singleton(UPGRADE), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), now(), f.getLanguage(), f.getWorkplace(),
                f.getGoals() == null ? singleton(UPGRADE) : f.getGoals(), authUserId());
    }
    public static Freshen asNewFreshen(String language, String workplace, Goal goal){
        return new Freshen(null, now(), language, workplace, singleton(goal == null ? UPGRADE : goal), authUserId());
    }

    public static  Freshen scheduledFreshen(String workplace) {
        return new Freshen(null, now(), "java", workplace, singleton(UPGRADE), ADMIN_ID);
    }
}
