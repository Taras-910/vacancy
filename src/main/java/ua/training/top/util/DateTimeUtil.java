package ua.training.top.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final Date DATE_TEST = toDate(2020, 7, 30);
    public static final LocalDateTime LOCAL_DATE_TIME_TEST = LocalDateTime.of(2020, 7, 30, 12, 0);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static Date thisDay = clearTime(new Date());
/*

    public static final LocalTime TIME_TEST_IN = LocalTime.now().plusMinutes(10);
    public static final LocalTime TIME_TEST_OUT = LocalTime.now().minusMinutes(10);

    public static LocalTime сhangeVoteTime = LocalTime.of(11, 0);


    public static void setСhangeVoteTime(LocalTime сhangeVoteTime) {
        DateTimeUtil.сhangeVoteTime = сhangeVoteTime;
    }

    public static void setThisDay(Date thisDay) {
        DateTimeUtil.thisDay = thisDay;
    }
*/

    public static @Nullable LocalTime parseLocalTime(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalTime.parse(str);
    }

    public static @Nullable LocalDate parseLocalDate(@Nullable String str) {
        return StringUtils.isEmpty(str) ? null : LocalDate.parse(str);
    }

    public static Date parse(String text, Locale locale) {
        Date localDate;
        try {
            localDate = new SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (ParseException e) {
            throw new IllegalArgumentException("error argument localDate=" + text);
        }
        return localDate;
    }

    public static Date getTimestamp(@Nullable int year, @Nullable int month, @Nullable int day){
        return new Timestamp(toDate(year, month, day).getTime());
    }

    public static @Nullable Date toDate(@Nullable int year, @Nullable int month, @Nullable int day) {
        Date date = new Date(year - 1900, month - 1, day);
        return clearTime(date);
    }

    public static Date clearTime(Date date) {
        date.setSeconds(0);
        date.setMinutes(0);
        date.setHours(0);
        return date;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

//    public static String print(Date dt) {
//        return new SimpleDateFormat(DATE_PATTERN).format(dt);
//    }

    public static String print(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DATE_FORMATTER);
    }

}
