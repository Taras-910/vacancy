package ua.training.top.util.jsoup.datas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorrectTitle {
    public static final Logger log = LoggerFactory.getLogger(CorrectTitle.class);

    public static String getCorrectTitle(String companyTitle){
        return companyTitle.contains("(ID") ? companyTitle.substring(0, companyTitle.indexOf("(ID")).trim() : companyTitle;
    }

}
