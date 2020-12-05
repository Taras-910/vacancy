package ua.training.top.aggregator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.training.top.aggregator.util.rate.RateUtil.*;

public class ToCorrectDataUtil {
    public static final Logger log = LoggerFactory.getLogger(ToCorrectDataUtil.class);

    public static String getCorrectCompanyName(String companyName){
        companyName = companyName.isEmpty() ? " не указано" : companyName.trim();
        companyName = companyName.contains(" Профиль компании") ? companyName.replace("Профиль компании", "").trim() : companyName;
        companyName = companyName.contains(",") ? companyName.split(",")[0].trim() : companyName;
        return companyName;
    }

    public static String getCorrectTitle(String companyTitle){
        return companyTitle.contains("(ID") ? companyTitle.substring(0, companyTitle.indexOf("(ID")).trim() : companyTitle;
    }

    public static String getCorrectCity(String city){
        return city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
    }

    public  static String getCorrectSalary(String salary){
        try {
//        log.info("salary={}", salary);
            salary = salary.toLowerCase();
            if (!checkSalary(salary) || salary.equals("")) {
                return "1";
            }

            String temp = salary.replace(" ", "").replace("–", "-")
                    .replace(" ", "").replace(" ","");
//        log.info("salary={}", salary);
//        log.info("temp={}", temp);
            if (salary.contains("$") || salary.contains("usd")){
                String cleaned = cleanSalary(temp).trim();
                salary = temp.contains("до") ? "1".concat("-").concat(cleaned).concat("00") : cleaned;
                salary = temp.contains("от") ? cleaned.concat("00-").concat("1") : salary;
                salary = temp.contains("-") ? cleaned.replace("-", "00-").concat("00") : salary;
            }

            if (salary.contains("$") && salary.contains("грн")){
                String cleaned = cleanSalary(temp).trim();
                salary = temp.contains("·") ? "1".concat("-").concat(temp.substring(temp.indexOf("$")+1)).concat("00") : cleaned;
            }

            if (salary.contains("eur")){
                String cleaned = cleanSalary(temp).trim();
    //            log.info("temp={}", temp);
    //            log.info("cleaned={}", cleaned);

                if(temp.contains("-")){
                    if(cleaned.length() < 5) return "1";
                    return eurToUsd(cleaned.split("-")[0]).concat("-").concat(eurToUsd(cleaned.split("-")[1]));
                }
                salary = cleaned.contains("до") ? "1".concat("-").concat(eurToUsd(cleaned)) : cleaned;
                salary = cleaned.contains("от") ? eurToUsd(cleaned).concat("-").concat("1") : salary;
            }

            if (salary.contains("грн")){
                String cleaned = cleanSalary(temp).trim();
                if(temp.contains("-")){
                    if(cleaned.length() < 5) return "1";
                    salary = hrnToUsd(cleaned.split("-")[0]).concat("-").concat(hrnToUsd(cleaned.split("-")[1]));
                }
                else {
                    cleaned = hrnToUsd(cleaned);
                    salary = temp.contains("до") ? "1".concat("-").concat(cleaned) : cleaned;
                    salary = temp.contains("от") ? cleaned.concat("-").concat("1") : salary;
                }
            }

            if (salary.contains("pln")){
                temp = temp.substring(temp.indexOf(":"), temp.lastIndexOf("("));
                String cleaned = cleanSalary(temp).trim();
                if(temp.contains("-")){
                    if(cleaned.length() < 5) return "1";
                    if(salary.contains("month")) {
                        return plnToUsd(cleaned.split("k-")[0].concat("00")).concat("-").concat(plnToUsd(cleaned.split("-")[1].concat("00")));
                    }
                    if(salary.contains("hour")) {
                        String from = String.valueOf(Float.parseFloat(cleaned.split("-")[0]) * 8 * 20 / 1000);
                        String to = String.valueOf(Float.parseFloat(cleaned.split("-")[1]) * 8 * 20 / 1000);
                        return plnToUsd(from).concat("-").concat(plnToUsd(to));
                    }
                } else{
                    if(salary.contains("month")){
                        return  "1".concat("-").concat(plnToUsd(cleaned));
                    }
                    if(salary.contains("hour")){
                        return "1".concat("-").concat(plnToUsd(String.valueOf(Float.parseFloat(cleaned) * 8 * 20 / 1000)));
                    }
                }
                    salary = "1".concat("-").concat(plnToUsd(cleaned.concat("00")));
            }
        } catch (NumberFormatException e) {
            log.info("Exception for parse salary={}", salary);
            return "1";
        }
        return salary;
    }

    public static String cleanSalary(String salary) {
        salary = salary.replaceAll("[a-zф-я.~,;+]", "");
        Matcher matcher = Pattern.compile("\\d.*\\d").matcher(salary);
        while (matcher.find()) {
            salary = salary.substring(matcher.start(), matcher.end());
        }

        return salary;
    }

    private static boolean checkSalary(String salary) {
        return cleanSalary(salary).isEmpty()
                || salary.contains("salary") && salary.contains("pln") || salary.contains("грн")
                || salary.contains("$") || salary.contains("usd") || salary.contains("eur");
    }
}
