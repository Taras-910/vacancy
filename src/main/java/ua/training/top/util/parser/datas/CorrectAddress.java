package ua.training.top.util.parser.datas;

public class CorrectAddress {
    public static String getCorrectAddress(String city){
        return city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
    }

    public static String getTranslated(String city){
        switch (city){
            case "киев": city = "kyiv";
                break;
            case "днепр": city = "dnipro";
                break;
            case "харьков": city = "kharkiv";
                break;
            case "одесса": city = "odesa";
                break;
            case "львов": city = "lviv";
                break;
            case "николаев": city = "mykolaiv";
                break;
            case "винница": city = "vinnitsia";
                break;
            case "запорожье": city = "zaporizhzhya";
                break;
            case "черновцы": city = "chernivtsi";
                break;
            case "чернигов": city = "chernigiv";
                break;
            case "ивано-франковск": city = "ivano-frankivsk";
                break;
            case "ужгород": city = "uzhgorod";
                break;
            case "удаленно": city = "remote";
                break;
        }
            return city;
    }

    public static String getCodeGrc(String city){
        switch (city){
            case "киев" : city = "115";
                break;
            case "одесса": city = "2188";
                break;
            case "львов": city = "2180";
                break;
            case "харьков": city = "2206";
                break;
            case "санкт-петербург" : city = "2";
                break;
            case "москва": city = "1";
                break;
            case "новосибирск": city = "1202";
                break;
            case "нижний новгород": city = "1679";
                break;
            case "казань": city = "1624";
                break;
            case "екатеринбург": city = "1261";
                break;
            case "краснодар": city = "1438";
                break;
            case "пермь": city = "1317";
                break;
            case "минск": city = "1002";
                break;
            case "ростов-на-дону": city = "1530";
                break;
            case "томск": city = "1255";
                break;
            case "самара": city = "1586";
                break;
            case "ульяновск": city = "1614";
                break;
            case "воронеж": city = "1844";
                break;
        }
        return city;
    }

    public static String getCodeHabr(String city){
        switch (city){
            case "киев" : city = "908";
                break;
            case "одесса": city = "912";
                break;
            case "львов": city = "711";
                break;
            case "харьков": city = "742";
                break;
            case "санкт-петербург" : city = "679";
                break;
            case "москва": city = "678";
                break;
            case "новосибирск": city = "717";
                break;
            case "нижний новгород": city = "715";
                break;
            case "казань": city = "698";
                break;
            case "екатеринбург": city = "693";
                break;
            case "краснодар": city = "707";
                break;
            case "пермь": city = "722";
                break;
            case "минск": city = "713";
                break;
            case "ростов-на-дону": city = "726";
                break;
            case "томск": city = "736";
                break;
            case "самара": city = "728";
                break;
            case "ульяновск": city = "739";
                break;
            case "воронеж": city = "692";
                break;
        }
        return city;
    }

}
