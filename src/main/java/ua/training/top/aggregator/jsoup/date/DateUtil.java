package ua.training.top.aggregator.jsoup.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static ua.training.top.aggregator.jsoup.date.MonthUtil.getMonth;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_PATTERN_STRATEGY = "dd.MM.yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String printStrategyRabota(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN_STRATEGY));
    }

    public static String printStrategyJobs(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date());
    }

    public static String getCurrentYear() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date()).substring(0,4);
    }

    public static String supportDate(String dateTime){
        if (dateTime == null || dateTime.equals("")) {
            return LocalDate.now().minusWeeks(1).toString();
        }
        String[] date = dateTime.split(" ");
        if (date.length < 3){
            if(LocalDate.now().getMonth().equals("JANUARY") && dateTime.contains("декабря")){
                dateTime.concat(" ").concat(String.valueOf(LocalDate.now().minusYears(1).getYear()));
            } else {
                dateTime.concat(" ").concat(getCurrentYear());
            }
        }
        StringBuilder sb = new StringBuilder(getCurrentYear());
        sb.append("-").append(getMonth(dateTime.split(" ")[1])).append("-");
        String day = dateTime.split(" ")[0];
        if (day.length() == 2) {
            sb.append(day);
        } else {
            sb.append("0").append(day);
        }
        return sb.toString();
    }

}
