package ua.training.top.util;

import org.springframework.util.StringUtils;
import ua.training.top.model.Freshen;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;

import static ua.training.top.SecurityUtil.authUserId;

public class FreshenUtil {
    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, LocalDateTime.now(), vTo.getLanguage(),
                StringUtils.hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), f.getRecordedDate() == null ? LocalDateTime.now() : f.getRecordedDate(),
                f.getLanguage(), f.getWorkplace(), authUserId());
    }


}
