package ua.training.top.aggregator.jsoup.datas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToCorrectTitle {
    public static final Logger log = LoggerFactory.getLogger(ToCorrectTitle.class);

    public static String getCorrectTitle(String companyTitle){
        return companyTitle.contains("(ID") ? companyTitle.substring(0, companyTitle.indexOf("(ID")).trim() : companyTitle;
    }

}
