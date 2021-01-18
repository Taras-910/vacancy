package ua.training.top.util.refresh.datas;

public class CorrectAddress {
    public static String getCorrectAddress(String city){
        return city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
    }
}
