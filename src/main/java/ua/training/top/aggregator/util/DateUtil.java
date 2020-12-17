package ua.training.top.aggregator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_PATTERN_STRATEGY = "dd.MM.yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    public static LocalDate parseLocalDate( String str) {
        return str.isEmpty() ? null : LocalDate.parse(str);
    }

    public static String print(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DATE_FORMATTER);
    }

    public static String printStrategyRabota(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN_STRATEGY));
    }

    public static String printDefault(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String print(Date dt) {
        return new SimpleDateFormat(DATE_PATTERN).format(dt);
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

    public static String getCurrentYear() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date()).substring(0,4);
    }

    public static String getMonth(String month){
        String result = "01";
        switch (month){
            case "января"  : result = "01";
                break;
            case "февраля" : result = "02";
                break;
            case "марта"    : result = "03";
                break;
            case "апреля"  : result = "04";
                break;
            case "мая"     : result = "05";
                break;
            case "июня"    : result = "06";
                break;
            case "июля"    : result = "07";
                break;
            case "августа"  : result = "08";
                break;
            case "сентября": result = "09";
                break;
            case "октября" : result = "10";
                break;
            case "ноября"  : result = "11";
                break;
            case "декабря" : result = "12";
                break;
        }
        return result;
    }

    public static String getDateHH(String date){
        if (date == null || date.equals("")) {
            return LocalDate.now().minusWeeks(1).toString();
        }
        date = date.trim();
        if (date.split(" ").length < 3){
            date.concat(" ").concat(getCurrentYear());
        }

        StringBuilder sb = new StringBuilder(getCurrentYear());
        sb.append("-").append(getMonth(date.split(" ")[1])).append("-");
        String day = date.split(" ")[0];
        if (day.length() == 2) {
            sb.append(day);
        } else {
            sb.append("0").append(day);
        }
        return sb.toString();
    }

    public static String getCorrectDate(String myDate){

        if(myDate == null || myDate.isEmpty()){
            return LocalDate.now().minusDays(7).toString();
        }
        myDate = myDate.toLowerCase().trim();
        try {
            if(myDate.contains("мин") || myDate.contains("хв") || myDate.contains("сегодня") || myDate.contains("только что")){
                return print(LocalDate.now());
            }
            if(myDate.contains("ч") || myDate.contains("час") || myDate.contains("год")){
                    return LocalDateTime.now().minusHours(Integer.parseInt(myDate.replaceAll("\\W+", "").trim())).toLocalDate().toString();
            }
            if(myDate.length() > 1 && (myDate.contains("місяц") || myDate.contains("месяц"))){
                myDate = myDate.contains("більше") ? myDate.replace("більше", "").trim() : myDate;
                return print(LocalDate.now().minusMonths(Integer.parseInt(myDate.substring(0, 2).trim())).minusDays(1));
            }
            if(myDate.length() > 1 && (myDate.contains("день") || myDate.contains("дн"))){
                return print(LocalDate.now().minusDays(Integer.parseInt(myDate.trim().substring(0, 2).trim())));
            }
            if(myDate.length() > 1 && myDate.contains("вчера")){
                return print(LocalDate.now().minusDays(1));
            }
        } catch (NumberFormatException e) {
            log.info("Wrong data {} exception {}", myDate, e.getMessage());
            return LocalDate.now().minusDays(7).toString();
        }
        return getCurrentDate();
    }
}