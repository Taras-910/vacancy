package ua.training.top.util.parser.date;

public class MonthUtil {

    public static String getMonth(String month){
        String result = "01";
        switch (month){
            case "Jan"    :
            case "січня"    :
            case "января"   : result = "01";
                break;
            case "Feb"   :
            case "лютого"   :
            case "февраля"  : result = "02";
                break;
            case "Mar"  :
            case "березня"  :
            case "марта"    : result = "03";
                break;
            case "Apr"   :
            case "квітня"   :
            case "апреля"   : result = "04";
                break;
            case "May"   :
            case "травня"   :
            case "мая"      : result = "05";
                break;
            case "Jun"   :
            case "червня"   :
            case "июня"     : result = "06";
                break;
            case "Jul"    :
            case "липня"    :
            case "июля"     : result = "07";
                break;
            case "Aug"   :
            case "серпня"   :
            case "августа"  : result = "08";
                break;
            case "Sep"  :
            case "вересня"  :
            case "сентября" : result = "09";
                break;
            case "Oct"   :
            case "жовтня"   :
            case "октября"  : result = "10";
                break;
            case "Nov":
            case "листопада":
            case "ноября"   : result = "11";
                break;
            case "Dec"   :
            case "грудня"   :
            case "декабря"  : result = "12";
                break;
        }
        return result;
    }
}
