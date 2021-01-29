package ua.training.top.util.parser.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static ua.training.top.util.parser.date.MonthUtil.getMonth;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_PATTERN_STRATEGY = "dd.MM.yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String printStrategyRabota(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN_STRATEGY));
    }

    public static String getCurrentYear() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date()).substring(0,4);
    }

    public static String supportDate(String dateTime){
        if (dateTime.split(" ").length < 2 || dateTime == null || dateTime.isEmpty()) {
            return LocalDate.now().minusWeeks(1).toString();
        }
        dateTime = dateTime.split(" ").length < 3 ? toAddYear(dateTime) : dateTime;
        String[] dateParts = dateTime.split(" ");
        StringBuilder sb = new StringBuilder(dateParts[2]);
        sb.append("-").append(getMonth(dateParts[1])).append("-");
        return sb.append(dateParts[0].length() == 2 ? dateParts[0] : "0".concat(dateParts[0])).toString();
    }

    private static String toAddYear(String dateTime) {
        if(LocalDate.now().getMonth().toString().equals("JANUARY") && dateTime.contains("декабря")){
            return dateTime.concat(" ").concat(String.valueOf(LocalDate.now().minusYears(1).getYear()));
        } else {
            return dateTime.concat(" ").concat(getCurrentYear());
        }
    }
}
