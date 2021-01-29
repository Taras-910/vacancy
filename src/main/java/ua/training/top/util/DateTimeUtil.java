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

import static ua.training.top.aggregator.strategy.installation.InstallationUtil.freshenPerHour;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final String TIME_PATTERN = "HH:mm";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_PATTERN);
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final LocalDate DATE_TEST = LocalDate.of(2020, 7, 30);
    public static final LocalDateTime LOCAL_DATE_TIME_TEST = LocalDateTime.of(2020, 7, 30, 12, 0);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final  LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();
    public static final LocalDateTime yesterday = LocalDate.now().minusDays(1).atStartOfDay();
    public static final  LocalDateTime lastHour = LocalTime.now().plusHours(1).atDate(LocalDate.now());
    public static final LocalDateTime nextHour = LocalTime.now().minusHours(1).atDate(LocalDate.now());

    public static LocalDate thisDay = LocalDate.now();

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static String toStringTime(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(TIME_FORMATTER);
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
                            toStringTime(lastFreshen.getRecordedDate().plusMinutes(60/freshenPerHour).plusMinutes(1)));
                }
            }
        }
    }

}
