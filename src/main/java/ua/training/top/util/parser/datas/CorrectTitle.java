package ua.training.top.util.parser.datas;

public class CorrectTitle {
    public static String getCorrectTitle(String title){
        return title.contains("(ID") ? title.substring(0, title.indexOf("(ID")).trim() : title;
    }
}
