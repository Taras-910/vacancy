package ua.training.top.util.parser.datas;

public class CorrectAddress {
    public static String getCorrectAddress(String city){
        return city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
    }
}
