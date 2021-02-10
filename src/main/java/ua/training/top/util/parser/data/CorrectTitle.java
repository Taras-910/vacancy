package ua.training.top.util.parser.data;

import static org.springframework.util.StringUtils.hasText;

public class CorrectTitle {
    public static String getCorrectTitle(String title){
        title = title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : title;
        title = title.contains("Java Script") ? title.replaceAll("Java Script", "JavaScript") : title;
        return hasText(title) ? title.trim() : "see the card on the link";
    }
}
