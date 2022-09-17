package ua.training.top.util.collect.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.LocalDate.now;
import static java.time.LocalDate.parse;
import static ua.training.top.util.collect.data.DataUtil.*;
import static ua.training.top.util.collect.data.PatternUtil.*;

public class DateToUtil {
    private final static Logger log = LoggerFactory.getLogger(DateToUtil.class);
    public static final LocalDate defaultDate = LocalDate.now().minusDays(7);

    public static LocalDate getToLocalDate(String originText) {
        String preText = formatToNumAndWord(originText.toLowerCase());
        String text = getExtract(local_date, preText);
        boolean isDateNumber = pattern_is_date_numbers.matcher(preText).find();
        if (isEmpty(preText) || !text.contains(" ") && !isDateNumber) {
            return defaultDate;
        }
        try {
            if (!isDateNumber) {
                String[] parts = text.split(" ");
                int number = Integer.parseInt(parts[pattern_date_is_numb.matcher(parts[0]).find() ? 0 : 1]);
                String name = parts[pattern_date_is_numb.matcher(parts[1]).find() ? 0 : 1];
                LocalDate localDate = getLocalDate(number, name);
                return localDate.isBefore(now()) || localDate.isEqual(now()) ? localDate : localDate.minusYears(1);
            }
            return parse(text.contains("t") ? text.substring(0, text.indexOf("t")) : text);
        } catch (Exception e) {
            log.error(error, e.getMessage(), originText);
            return defaultDate;
        }
    }

    public static String getExtract(String fieldName, String text) {
        getLinkIfEmpty(text);
        //https://stackoverflow.com/questions/63964529/a-regex-to-get-any-price-string
        Matcher m = getPatternByField(fieldName).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list.size() > 0 ? list.get(0) : !fieldName.contains("field") ? text : link;
    }

    public static Pattern getPatternByField(String fieldName) {
        return switch (fieldName) {
            case month -> pattern_extract_month;
            case local_date -> pattern_extract_date;
            case address_field -> pattern_extract_address;
            case age_field -> pattern_extract_age;
            default -> pattern_default;
        };
    }

    static LocalDate getLocalDate(int number, String name) {
        return isMatch(monthsOfYearAria, name) ? LocalDate.of(now().getYear(), getMonth(name), number) :
                switch (name) {
                    case "nowa", "сейчас", "минуту", "минуты", "минут" -> LocalDateTime.now().minusMinutes(number).toLocalDate();
                    case "годину", "години", "годин", "час", "часа", "часов" -> LocalDateTime.now().minusHours(number).toLocalDate();
                    case "день", "дня", "дней", "днів", "дні", "сьогодні", "сегодня" -> now().minusDays(number);
                    case "неделя", "недели", "неделю" -> now().minusWeeks(number);
                    case "месяц", "месяца" -> now().minusMonths(number);
                    default -> defaultDate;
                };
    }

    static String formatToNumAndWord(String originText) {
        return originText.replaceAll("nowa", "0 минут").replaceAll("сейчас", "0 минут")
                .replaceAll("только что", "0 минут")
                .replaceAll("сьогодні", "0 сьогодні").replaceAll("сьогодні", "0 сьогодні")
                .replaceAll("today", "0 сегодня").replaceAll("yesterday", "1 сьогодні")
                .replaceAll("вчора", "1 сьогодні")
                .replaceAll("вчера", "1 день");
    }

    public static int getMonth(String month) {
        return switch (month) {
            case "feb", "лютого", "февраля" -> 2;
            case "mar", "березня", "марта" -> 3;
            case "apr", "квітня", "апреля" -> 4;
            case "may", "травня", "мая" -> 5;
            case "jun", "червня", "июня" -> 6;
            case "jul", "липня", "июля" -> 7;
            case "aug", "серпня", "августа" -> 8;
            case "sep", "вересня", "сентября" -> 9;
            case "oct", "жовтня", "октября" -> 10;
            case "nov", "листопада", "ноября" -> 11;
            case "dec", "грудня", "декабря" -> 12;
            default -> 1;
        };
    }
}
