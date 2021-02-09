package ua.training.top.util;

import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;

public class FreshenUtil {
    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, LocalDateTime.now(), vTo.getLanguage(),
                hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), f.getRecordedDate() == null ? LocalDateTime.now() : f.getRecordedDate(),
                f.getLanguage(), f.getWorkplace(), authUserId());
    }
    public static Freshen asNewFreshen(String language, String workplace){
        return new Freshen(null, LocalDateTime.now(), language, workplace, authUserId());
    }
}
