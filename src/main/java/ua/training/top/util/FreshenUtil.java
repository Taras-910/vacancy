package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.model.Goal;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;

public class FreshenUtil {
    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, LocalDateTime.now(), vTo.getLanguage(),
                hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), Collections.singleton(Goal.UPGRADE), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), f.getRecordedDate() == null ? LocalDateTime.now() : f.getRecordedDate(),
                f.getLanguage(), f.getWorkplace(), f.getGoals(), authUserId());
    }
    public static Freshen asNewFreshen(String language, String workplace, Goal goal){
        return new Freshen(null, LocalDateTime.now(), language, workplace, Collections.singleton(goal), authUserId());
    }
}
