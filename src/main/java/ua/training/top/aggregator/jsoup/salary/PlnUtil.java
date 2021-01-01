package ua.training.top.aggregator.jsoup.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.aggregator.jsoup.salary.RateUtil.plnToUsd;

public class PlnUtil {
    public static final Logger log = LoggerFactory.getLogger(PlnUtil.class);

    public static String getPln(String salary) throws NumberFormatException{
        String temp = salary.replaceAll("–", "—");
        try {
            String cleaned = temp.replaceAll("[^—\\d]", "");
            if(cleaned.matches("\\d+\\W\\d+")){
                if(cleaned.length() < 4) return "1";
                if(salary.contains("year")) {
                    return plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned.split("\\W")[0]) / 12)))
                            .concat("—").concat(plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned.split("—")[1]) / 12))));
                }
                if(salary.contains("month")) {
                    return plnToUsd(cleaned.split("\\W")[0]).concat("—").concat(plnToUsd(cleaned.split("—")[1]));
                }
                if(salary.contains("day")) {
                    return plnToUsd(getSpring(cleaned.split("\\W")[0], 22)).concat("—")
                            .concat(plnToUsd(getSpring(cleaned.split("\\W")[1], 22)));
                }
                if(salary.contains("hour")) {
                    return plnToUsd(getSpring(cleaned.split("\\W")[0], 8 * 22)).concat("—")
                            .concat(plnToUsd(getSpring(cleaned.split("\\W")[1], 8 * 22)));
                }
            } else{
//String.valueOf((int)(Float.parseFloat(s) * factor))
                if(salary.contains("year")){
                    return  "1".concat("—").concat(plnToUsd(String.valueOf((int)(Float.parseFloat(cleaned) / 12))));
                }
                if(salary.contains("month")){
                    return  "1".concat("—").concat(plnToUsd(cleaned));
                }
                if(salary.contains("hour")){
                    return "1".concat("—").concat(plnToUsd(String.valueOf(Float.parseFloat(cleaned) * 8 * 20 / 1000)));
                }
            }
            salary = "1".concat("—").concat(plnToUsd(cleaned.concat("00")));
        } catch (NumberFormatException e) {
            log.info("there is exception on getCorrectSalary method during parse line:\n{}\n", salary);
        }
        return salary;
    }

    private static String getSpring(String s, int factor) {
        return String.valueOf((int)(Float.parseFloat(s) * factor));
    }
}
