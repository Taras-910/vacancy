package ua.training.top.util.parser.data;

public class CorrectTitle {
    public static String getCorrectTitle(String title){
        title = title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : title;
        title = title.contains("Java Script") ? title.replaceAll("Java Script", "JavaScript") : title;
        return title;
    }
}
