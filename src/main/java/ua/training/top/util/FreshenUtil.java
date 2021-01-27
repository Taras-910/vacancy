package ua.training.top.util;

import ua.training.top.SecurityUtil;
import ua.training.top.model.Freshen;
import ua.training.top.model.Role;
import ua.training.top.to.VacancyTo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.strategy.installation.InstallationUtil.freshenPerHour;
import static ua.training.top.util.DateTimeUtil.*;

public class FreshenUtil {
    public static Freshen getFreshenFromTo(VacancyTo vTo) {
        return new Freshen(null, LocalDateTime.now(), vTo.getLanguage(),
                hasText(vTo.getWorkplace()) ? vTo.getWorkplace() :vTo.getAddress(), authUserId());
    }

    public static Freshen asNewFreshen(Freshen f){
        return new Freshen(f.getId(), f.getRecordedDate() == null ? LocalDateTime.now() : f.getRecordedDate(),
                f.getLanguage(), f.getWorkplace(), authUserId());
    }

    public static void checkLimitTime(Freshen freshen, List<Freshen> freshensToday) {
        if(freshensToday != null || !SecurityUtil.get().getUser().getRoles().contains(Role.ADMIN)) {
            List<Freshen> freshensHour = freshensToday.stream()
                    .filter(f -> f.equals(freshen))
                    .filter(f -> f.getRecordedDate().isBefore(lastHour) && f.getRecordedDate().isAfter(nextHour))
                    .sorted((f1, f2) -> f2.getRecordedDate().compareTo(f1.getRecordedDate()))
                    .collect(Collectors.toList());
            if (!freshensHour.isEmpty()){
                Freshen lastFreshen = freshensHour.get(0);
                if (freshensHour.size() >= freshenPerHour ||
                        freshen.getRecordedDate().isBefore(lastFreshen.getRecordedDate().plusMinutes(60/freshenPerHour))) {
                    throw new IllegalStateException("По параметрам: {" + freshen.getLanguage()
                            + ", " + freshen.getWorkplace() + "} база данных<br>обновлялась последний раз в " +
                            toStringTime(lastFreshen.getRecordedDate()) + ",<br>повторите запрос после: " +
                            toStringTime(lastFreshen.getRecordedDate().plusMinutes(60/freshenPerHour)));
                }
            }
        }
    }
}
