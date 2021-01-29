package ua.training.top.util.parser.date;

public class MonthUtil {

    public static String getMonth(String month){
        String result = "01";
        switch (month){
            case "января"  : result = "01";
                break;
            case "февраля" : result = "02";
                break;
            case "марта"    : result = "03";
                break;
            case "апреля"  : result = "04";
                break;
            case "мая"     : result = "05";
                break;
            case "июня"    : result = "06";
                break;
            case "июля"    : result = "07";
                break;
            case "августа"  : result = "08";
                break;
            case "сентября": result = "09";
                break;
            case "октября" : result = "10";
                break;
            case "ноября"  : result = "11";
                break;
            case "декабря" : result = "12";
                break;
        }
        return result;
    }
}
