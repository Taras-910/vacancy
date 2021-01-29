package ua.training.top.util.parser.date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ToCorrectDate {
    private static final Logger log = LoggerFactory.getLogger(ToCorrectDate.class);

    public static LocalDate getCorrectDate(String myDate){
        if(myDate == null || myDate.isEmpty()){
            return LocalDate.now().minusDays(7);
        }
        myDate = myDate.toLowerCase().trim();
        try {
            if(myDate.length() > 1 && (myDate.contains("вчера") || myDate.contains("вчора"))){
                return LocalDate.now().minusDays(1);
            }
            if(myDate.contains("мин") || myDate.contains("хв") || myDate.contains("сьогодні") || myDate.contains("сегодня") || myDate.contains("только что")){
                return LocalDate.now();
            }
            if(myDate.contains("ч") || myDate.contains("час") || myDate.contains("год")){
                return LocalDateTime.now().minusHours(Integer.parseInt(myDate.replaceAll("\\W+", "").trim())).toLocalDate();
            }
            if(myDate.length() > 1 && (myDate.contains("місяц") || myDate.contains("месяц"))){
                myDate = myDate.contains("більше") ? myDate.replace("більше", "").trim() : myDate;
                return LocalDate.now().minusMonths(Integer.parseInt(myDate.substring(0, 2).trim())).minusDays(1);
            }
            if(myDate.length() > 1 && (myDate.contains("день") || myDate.contains("дн"))){
                return LocalDate.now().minusDays(Integer.parseInt(myDate.trim().substring(0, 2).trim()));
            }
        } catch (NumberFormatException e) {
            log.info("Wrong data {} exception {}", myDate, e.getMessage());
            return LocalDate.now().minusDays(7);
        }
        return LocalDate.now().minusDays(7);
    }
}
