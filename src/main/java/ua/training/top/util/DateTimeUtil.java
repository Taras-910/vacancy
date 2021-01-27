package ua.training.top.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
}
