package ua.training.top.util.parser.salary;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinMax {
    public static final Logger log = LoggerFactory.getLogger(MinMax.class);

    public static Integer salaryMin(String salary, Element element) {
        Integer result;
        if(salary.equals("") || salary.matches(".*\\W\\d\\W+")){
            return 1;
        }
        try {
            result = salary.matches("\\d+\\W\\d+") ?  Integer.parseInt(salary.split("\\W")[0]) : Integer.parseInt(salary);
        } catch (NumberFormatException e) {
            log.error("there is exception={} on salaryMin={} \nelement=\n{}\n", e.getLocalizedMessage(), salary, element);
            return 1;
        }
        if(result >= 10000000 || result < 1 ){
            log.info("there is wrong data on salaryMin={}", result);
            return 1;
        }
        return result;
    }

    public static Integer salaryMax(String salary, Element element) {
        Integer result;
        if(salary.equals("") || salary.matches(".*\\W\\d\\W+")){
            return 1;
        }
        try {
            result = salary.matches("\\d+\\W\\d+") ?  Integer.parseInt(salary.split("\\W")[1]) : 1;
        } catch (NumberFormatException e) {
            log.error("there is exception={} on salaryMax={} \nelement=\n{}\n", e.getLocalizedMessage(), salary, element);
            return 1;
        }
        if(result >= 10000000 || result < 1 ){
            log.info("there is wrong data on salaryMin={}", result);
            return 1;
        }
        return result;
    }

}
