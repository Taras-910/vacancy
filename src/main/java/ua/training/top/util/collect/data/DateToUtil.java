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
                    case "nowa", "сейчас", "минуту", "минуты", "минут", "minut", "minute", "minuten", "сега", "минута", "минути" -> LocalDateTime.now().minusMinutes(number).toLocalDate();
                    case "годину", "години", "годин", "час", "часа", "часов", "hours", "hour", "stunden", "std.", "godzin", "godzina", "godziny" -> LocalDateTime.now().minusHours(number).toLocalDate();
                    case "день", "дня", "дней", "day", "days", "today", "tag", "tage", "tagen", "днів", "дні", "сьогодні", "сегодня", "dzień", "dni", "ден", "дни" -> now().minusDays(number);
                    case "неделя", "недели", "неделю", "week", "weeks", "wochen", "tydzień", "седмица" -> now().minusWeeks(number);
                    case "месяц", "месяца", "month", "months", "monat", "monate", "miesiąc", "miesiące", "месец", "месеци" -> now().minusMonths(number);
                    default -> defaultDate;
                };
    }

    static String formatToNumAndWord(String originText) {
        originText = isContains(originText, "vor") ?
                originText.replaceAll("vor ", "").replaceAll("einem", "1") : originText;
        return originText.replaceAll("nowa", "0 минут").replaceAll("сейчас", "0 минут")
                .replaceAll("только что", "0 минут")
                .replaceAll("сьогодні", "0 сьогодні").replaceAll("сегодня", "0 сьогодні")
                .replaceAll("today", "0 сегодня").replaceAll("днес", "0 сегодня")
                .replaceAll("heute", "0 сегодня")
                .replaceAll("yesterday", "1 сьогодні").replaceAll("вчера", "1 день")
                .replaceAll("вчора", "1 сьогодні").replaceAll("gestern", "1 сьогодні")
                .replaceAll("einem", "1");
    }

    public static int getMonth(String month) {
        return switch (month) {
            case "january", "jan", "січня", "января", "январь" -> 1;
            case "february", "feb", "лютого", "февраля", "февраль" -> 2;
            case "march", "mar", "березня", "марта", "март" -> 3;
            case "april", "apr", "квітня", "апреля", "апрель" -> 4;
            case "may", "травня", "мая", "май" -> 5;
            case "june", "jun", "червня", "июня", "июнь" -> 6;
            case "july", "jul", "липня", "июля", "июль" -> 7;
            case "august", "aug", "серпня", "августа", "август" -> 8;
            case "september", "sep", "вересня", "сентября", "сентябрь" -> 9;
            case "october", "oct", "жовтня", "октября", "октябрь" -> 10;
            case "november", "nov", "листопада", "ноября", "ноябрь" -> 11;
            case "december", "dec", "грудня", "декабря", "декабрь" -> 12;
            default -> LocalDate.now().getMonth().getValue();
        };
    }
}
