package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinMax {
    public static final Logger log = LoggerFactory.getLogger(MinMax.class);

    public static Integer salaryMin(String salary) {
        Integer result;
        if(salary.equals("") || salary.matches(".*\\W\\d\\W+")){
            return 1;
        }
        try {
            result = salary.contains("—") ?  Integer.parseInt(salary.split("\\W")[0]) : Integer.parseInt(salary);
        } catch (NumberFormatException e) {
            log.error("there is exception on getCorrectSalary method salaryMin data salary={}", salary);
            return 1;
        }
        return result;
    }

    public static Integer salaryMax(String salary) {
        Integer result;
        if(salary.equals("") || salary.matches(".*\\W\\d\\W+")){
            return 1;
        }
        try {
            result = salary.contains("—") ?  Integer.parseInt(salary.split("\\W")[1]) : 1;
        } catch (NumberFormatException e) {
            log.error("there is exception={} on salaryMax={}", e.getLocalizedMessage(), salary);
            return 1;
        }
        return result;
    }

}
