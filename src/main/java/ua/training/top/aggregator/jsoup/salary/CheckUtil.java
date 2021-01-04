package ua.training.top.aggregator.jsoup.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CheckUtil {
    public static final Logger log = LoggerFactory.getLogger(CheckUtil.class);

    public static boolean checkSalary(String salary) {
        return (salary.contains("грн") || salary.contains("$") || salary.contains("usd") || salary.contains("eur")
                || salary.contains("€") || salary.contains("pln") || salary.contains("salary"));
    }

    public static String validateAndFormat(String salary) {
        salary = salary.toLowerCase();
        if (salary.isEmpty() || salary.matches(".*\\W\\d\\W+") || !checkSalary(salary)) {
            return "1";
        }
        salary = salary.replaceAll(" ", "");
        salary = salary.replaceAll(" ", "");
        salary = salary.replaceAll("–", "—");
        salary = salary.replaceAll("-", "—");
        salary = salary.replaceAll("b2b", "");
        salary = salary.contains(",") ? salary.split(",")[0] : salary;
        salary = salary.contains("·") ? salary.split("·")[0] : salary;

        if(salary.matches(".*?\\.\\d?\\dk.*?")){
            String temp1 = salary.replaceAll("[A-Za-jl-zа-я ·:\\/(),]","");
            List<String> list = Arrays.stream(temp1.split("—")).filter(s -> s.matches("\\d+\\.\\dk.*")).collect(Collectors.toList());
            for(String find : list) {
                find = find.substring(0, find.indexOf("k"));
                String newString = String.valueOf((int) (Float.parseFloat(find) * 1000));
                salary = salary.replace(find, newString);
            }
        }
        return salary;
    }

    public static String getCleaned(String salary) {
        return salary.replaceAll("[^\\d+-]+—[^\\d+ ]+", "")
                .replaceAll("[^—\\d]", "");
    }
}
