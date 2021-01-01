package ua.training.top.aggregator.jsoup.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.aggregator.jsoup.salary.CheckUtil.validateAndFormat;
import static ua.training.top.aggregator.jsoup.salary.EurUtil.getEur;
import static ua.training.top.aggregator.jsoup.salary.HrnUtil.getHrn;
import static ua.training.top.aggregator.jsoup.salary.PlnUtil.getPln;
import static ua.training.top.aggregator.jsoup.salary.UsdUtil.getUsd;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);

    public static String getCorrectSalary(String salary){
        String temp = validateAndFormat(salary);
        try {
            if (salary.contains("salary:") && !(salary.contains("$") || salary.contains("usd"))) {
                return getPln(temp);
            }
            if (salary.contains("$") || salary.contains("usd")) {
                return getUsd(temp);
            } else
            if (salary.contains("eur") || salary.contains("€")) {
                return getEur(temp);
            } else
            if (salary.contains("грн")) {
                return getHrn(temp);
            }
        } catch (NumberFormatException e) {
            log.info("Exception for parse salary={}", salary);
            return "1";
        }
        return temp;
    }
}
