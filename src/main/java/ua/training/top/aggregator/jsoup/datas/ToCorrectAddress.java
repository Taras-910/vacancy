package ua.training.top.aggregator.jsoup.datas;

public class ToCorrectAddress {
    public static String getCorrectAddress(String city){
        return city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
    }
}
