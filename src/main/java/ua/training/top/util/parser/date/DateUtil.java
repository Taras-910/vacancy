package ua.training.top.util.parser.date;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static ua.training.top.util.DateTimeUtil.print;
import static ua.training.top.util.parser.date.MonthUtil.getMonth;
import static ua.training.top.util.parser.date.ToCorrectDate.getCorrectDate;

public class DateUtil {
    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
    public static final String DATE_PATTERN_STRATEGY = "dd.MM.yyyy";
    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static String printStrategyRabota(LocalDate ldt) {
        return ldt == null ? "" : ldt.format(DateTimeFormatter.ofPattern(DATE_PATTERN_STRATEGY));
    }

    public static String dateRabota() {
        return printStrategyRabota(LocalDate.now().minusDays(7));
    }

    public static String getCurrentYear() {
        return new SimpleDateFormat(DATE_PATTERN).format(new Date()).substring(0,4);
    }

    public static String prepare(String dateTime){
        String[] parts = dateTime.split(" ");
        StringBuilder sb = new StringBuilder();
        for(String p: parts){
            if(!p.contains(".")){
                sb.append(p).append(" ");
            }
        }
        return sb.toString().trim();
    }

    public static String supportDate(String dateTime){
        if (dateTime == null || dateTime.isEmpty()) {
            return LocalDate.now().minusWeeks(1).toString();
        }
        if (dateTime.split(" ").length < 2) {
            return print(getCorrectDate(dateTime));
        }
        dateTime = dateTime.split(" ").length < 3 ? toAddYear(dateTime) : dateTime;
        String[] dateParts = dateTime.split(" ");
        String partDay = dateParts[1].matches("\\d+") ? dateParts[1] : dateParts[0];
        String partMonth = dateParts[1].matches("\\d+") ? dateParts[0] : dateParts[1];

        StringBuilder sb = new StringBuilder(dateParts[2]);
        sb.append("-").append(getMonth(partMonth)).append("-");
        return sb.append(partDay.length() == 2 ? partDay : "0".concat(partDay)).toString();
    }

    private static String toAddYear(String dateTime) {
        if(LocalDate.now().getMonth().toString().equals("JANUARY") && dateTime.contains("декабря")){
            return dateTime.concat(" ").concat(String.valueOf(LocalDate.now().minusYears(1).getYear()));
        } else {
            return dateTime.concat(" ").concat(getCurrentYear());
        }
    }

    public static LocalDate parseCustom(String dateTime, Element element) {
        LocalDate localDate = LocalDate.now().minusDays(7);
        try {
            localDate = LocalDate.parse(dateTime);
        } catch (Exception e) {
            log.error("there is error for parse {} element {}", dateTime, element);
        }
        return localDate;
    }

    public static LocalDate getDateNofluffjobs(String text) {
        return text.contains("NEW") || text.contains("NOVĚ")? LocalDate.now() : LocalDate.now().minusDays(7);
    }
}
