package ua.training.top.util.jsoup.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.jsoup.salary.CheckUtil.validateAndFormat;
import static ua.training.top.util.jsoup.salary.EurUtil.getEur;
import static ua.training.top.util.jsoup.salary.HrnUtil.getHrn;
import static ua.training.top.util.jsoup.salary.PlnUtil.getPln;
import static ua.training.top.util.jsoup.salary.UsdUtil.getUsd;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);

    public static String getCorrectSalary(String salary){
        String temp = validateAndFormat(salary);
        try {
            if ((temp.contains("salary:") || temp.contains("pln") || temp.contains("(uop)"))&& !(salary.contains("$") || salary.contains("usd"))) {
                return getPln(temp);
            }
            if (salary.contains("$") || salary.contains("usd")) {
                return getUsd(temp);
            } else
            if (temp.contains("eur") || salary.contains("€")) {
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
