package ua.training.top.util.parser.date;

public class MonthUtil {

    public static String getMonth(String month){
        String result = "01";
        switch (month){
            case "січня"    :
            case "января"   : result = "01";
                break;
            case "лютого"   :
            case "февраля"  : result = "02";
                break;
            case "березня"  :
            case "марта"    : result = "03";
                break;
            case "квітня"   :
            case "апреля"   : result = "04";
                break;
            case "травня"   :
            case "мая"      : result = "05";
                break;
            case "червня"   :
            case "июня"     : result = "06";
                break;
            case "липня"    :
            case "июля"     : result = "07";
                break;
            case "серпня"   :
            case "августа"  : result = "08";
                break;
            case "вересня"  :
            case "сентября" : result = "09";
                break;
            case "жовтня"   :
            case "октября"  : result = "10";
                break;
            case "листопада":
            case "ноября"   : result = "11";
                break;
            case "грудня"   :
            case "декабря"  : result = "12";
                break;
        }
        return result;
    }
}
