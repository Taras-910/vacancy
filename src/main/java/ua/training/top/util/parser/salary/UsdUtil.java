package ua.training.top.util.parser.salary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ua.training.top.util.parser.salary.CheckUtil.getCleaned;

public class UsdUtil {
    public static final Logger log = LoggerFactory.getLogger(UsdUtil.class);

    public static String getUsd(String temp){
        String cleaned = getCleaned(temp);
        if (!temp.contains("грн")){
            if(cleaned.contains("—")){
                return cleaned.replace("—", "00—").concat("00");
            } else{
                return temp.contains("от") ? cleaned.concat("00—").concat("1") :"1".concat("—").concat(cleaned).concat("00");
            }
        } else {
            if (temp.contains("—")) {
                return cleaned.substring(temp.indexOf("$") + 1).replace("—", "00—").concat("00");
            } else {
                return temp.contains("·") ? "1".concat("—").concat(temp.substring(temp.indexOf("$") + 1)).concat("00") : cleaned;
            }
        }
    }
}
