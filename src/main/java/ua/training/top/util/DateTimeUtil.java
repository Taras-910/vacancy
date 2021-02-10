package ua.training.top.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import ua.training.top.SecurityUtil;
import ua.training.top.model.Freshen;
import ua.training.top.model.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.SecurityUtil.authUserId;
import static ua.training.top.aggregator.installation.InstallationUtil.*;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String DAY_AND_TIME_PATTERN = "HH:mm d MMM yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter DAY_AND_TIME_FORMATTER = DateTimeFormatter.ofPattern(DAY_AND_TIME_PATTERN);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();
    public static final LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
    public static final LocalDateTime lastHour = LocalTime.now().minusHours(1).atDate(LocalDate.now());
    public static final LocalDateTime nextHour = LocalTime.now().plusHours(1).atDate(LocalDate.now());
    public static final LocalDate testDate = LocalDate.of(2020, 7, 30);
    public static final LocalDate thisDay = LocalDate.now();

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static String toStringTime(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DAY_AND_TIME_FORMATTER);
    }

    public static String print(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DATE_FORMATTER);
    }

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static @Nullable LocalDateTime parseLocalDateTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDateTime.parse(str);
    }

    public static void checkLimitFreshenPerHour(Freshen freshen, List<Freshen> freshensToday) {
        if(!freshensToday.isEmpty()) {
            if(SecurityUtil.get().getUser().getRoles().contains(Role.ADMIN)) {
                freshenPerHour = freshenPerHourForAdmin;
                limitPerHourForAuth = limitPerHourForAdmin;
            }
            List<Freshen> freshensHour = freshensToday.stream()
                    .filter(f -> f.getRecordedDate().isAfter(lastHour) && f.getRecordedDate().isBefore(nextHour) && f.equals(freshen))
                    .sorted((f1, f2) -> f1.getRecordedDate().compareTo(f2.getRecordedDate()))
                    .collect(Collectors.toList());
            if (!freshensHour.isEmpty()){
                Freshen lastFreshen = freshensHour.get(0);
                if (freshensHour.size() >= freshenPerHour ||
                        freshen.getRecordedDate().isBefore(lastFreshen.getRecordedDate().plusMinutes((int)(60/freshenPerHour)))) {
                    throw new IllegalStateException("по этому запросу:    \'" + freshen.getLanguage() + "\', \'" +
                            freshen.getWorkplace() + " \' <br>последнее обновление было в " +
                            toStringTime(lastFreshen.getRecordedDate()) + "<br>повторите запрос после " +
                            toStringTime(lastFreshen.getRecordedDate().plusMinutes((int)(60/freshenPerHour)).plusMinutes(1)));
                }
            }
            List<Freshen> freshensAuth = freshensToday.stream()
                    .filter(f -> f.getUserId() == authUserId()
                            && f.getRecordedDate().getHour() < nextHour.getHour()
                            && f.getRecordedDate().getHour() >= lastHour.getHour())
                    .collect(Collectors.toList());
            if (freshensAuth.size() >= limitPerHourForAuth) {
                throw new IllegalStateException("Ваш лимит обновлений (" + (int)limitPerHourForAuth + "/час) исчерпан<br> повторите запрос после " + nextHour.getHour() + ".00");
            }
        }
    }
}
