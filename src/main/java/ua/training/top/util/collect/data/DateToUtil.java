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

public class DateToUtil {
    private final static Logger log = LoggerFactory.getLogger(DateToUtil.class);

    public static LocalDate getToLocalDate(String originText) {
        String preText = formatToNumAndWord(originText.toLowerCase());
        String text = getExtract(local_date, preText);
        if (isEmpty(preText) || !text.contains(" ") && !preText.matches(is_date_numbers)) {
            return defaultDate;
        }
        try {
            if (!preText.matches(is_date_numbers)) {
                String[] parts = text.split(" ");
                int number = Integer.parseInt(parts[parts[0].matches(is_date_number) ? 0 : 1]);
                String name = parts[parts[1].matches(is_date_number) ? 0 : 1];
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
        Matcher m = Pattern.compile(getMatcherByField(fieldName), Pattern.CASE_INSENSITIVE).matcher(text);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list.size() > 0 ? list.get(0) : !fieldName.contains("field") ? text : link;
    }

    public static String getMatcherByField(String fieldName) {
        return switch (fieldName) {
            case month -> extract_month;
            case local_date -> extract_date;
            case address_field -> extract_address;
            case age_field -> extract_age;
            default -> "";
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
                .replaceAll("сьогодні", "0 сьогодні").replaceAll("сегодня", "0 сегодня")
                .replaceAll("вчора", "1 сьогодні").replaceAll("вчера", "1 день");
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
