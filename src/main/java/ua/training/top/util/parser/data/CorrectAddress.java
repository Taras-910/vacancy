package ua.training.top.util.parser.data;

import ua.training.top.model.Freshen;

public class CorrectAddress {

    public static String getCorrectAddress(String city){
        city = city.contains("Агломерация") ? city.replace("Агломерация", "агломерация") : city;
        city = city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
        city = city.contains("віддалено") ? city.replaceAll("віддалено", "удаленно") : city;
        switch (city.toLowerCase()){
            case "киев": city = "Киев";
                break;
            case "днепр": city = "Днепр";
                break;
            case "харьков": city = "Харьков";
                break;
            case "одесса": city = "Одесса";
                break;
            case "львов": city = "Львов";
                break;
            case "николаев": city = "Николаев";
                break;
            case "винница": city = "Винница";
                break;
            case "запорожье": city = "Запорожье";
                break;
            case "черновцы": city = "Черновцы";
                break;
            case "чернигов": city = "Чернигов";
                break;
            case "Ивано-франковск": city = "Ивано-франковск";
                break;
            case "Ужгород": city = "Ужгород";
                break;
            case "минск": city = "Минск";
                break;
            case "удаленно":
            case "віддалена робота":
            case "no location": city = "удаленно";
                break;
            default: city = city == null || city.equals("") || city.length() < 2 ?
                    city : city.substring(0, 1).toUpperCase().concat(city.substring(1));
                break;
        }
        return city;
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
            case "санкт-петербург" :
            case "москва":
            case "новосибирск":
            case "нижний новгород":
            case "казань":
            case "екатеринбург":
            case "краснодар":
            case "пермь":
            case "минск":
            case "ростов-на-дону":
            case "томск":
            case "самара":
            case "ульяновск":
            case "воронеж": city = "-1";
        }
            return city;
    }

    public static String getCodeGrc(String city){
        switch (city){
            case "киев" : city = "115";
                break;
            case "днепр": city = "2126";
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
            case "за_рубежем":
            case "днепр":
            case "одесса":
            case "львов":
            case "николаев":
            case "запорожье":
            case "черновцы":
            case "чернигов":
            case "варшава":
            case "польша":
            case "харьков": city = "-1";
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

    public static boolean isMatchesWorkplaceRabotaIndeedJobs(String city){
        boolean matches = true;
        switch (city){
            case "санкт-петербург" :
            case "москва":
            case "новосибирск":
            case "нижний новгород":
            case "казань":
            case "екатеринбург":
            case "краснодар":
            case "пермь":
            case "ростов-на-дону":
            case "томск":
            case "самара":
            case "ульяновск":
            case "минск":
            case "воронеж": matches = false;
        }
        return matches;
    }

    public static String getCorrectWorkplaceJooble(String city){
        switch (city){
            case "киев": city = "Київ";
                break;
            case "днепр": city = "Дніпро";
                break;
            case "харьков": city = "Харків";
                break;
            case "одесса": city = "Одеса";
                break;
            case "львов": city = "Львів";
                break;
            case "николаев": city = "Миколаїв";
                break;
            case "винница": city = "Вінниця";
                break;
            case "запорожье": city = "Запоріжжя";
                break;
            case "черновцы": city = "Чорновці";
                break;
            case "чернигов": city = "Чернігів";
                break;
            case "ивано-франковск": city = "Івано-Франківськ";
                break;
            case "ужгород": city = "Ужгород";
                break;
            case "санкт-петербург" :
            case "москва":
            case "новосибирск":
            case "нижний новгород":
            case "казань":
            case "екатеринбург":
            case "краснодар":
            case "пермь":
            case "ростов-на-дону":
            case "томск":
            case "самара":
            case "ульяновск": city = "-1";
                break;
            case "минск": city = "Мінськ%2C%20Білорусь";
                break;
            case "варшава" : city = "Варшава%2C%20Польща";
                break;
            case "краков" : city = "Краків%2C%20Польща";
                break;
        }
        return city;
    }

    public static Object getCorrectNofluffjobs(String city) {
        switch (city){
            case "киев":
            case "днепр":
            case "харьков":
            case "одесса":
            case "львов":
            case "николаев":
            case "запорожье":
            case "черновцы":
            case "чернигов":
            case "ивано-франковск":
            case "ужгород":
            case "санкт-петербург" :
            case "москва":
            case "новосибирск":
            case "нижний новгород":
            case "казань":
            case "екатеринбург":
            case "краснодар":
            case "пермь":
            case "ростов-на-дону":
            case "томск":
            case "самара":
            case "минск":;
            case "ульяновск": city = "-1";
        }
        return city;
    }

    public static String getCorrectWorkplaceYandex(String city){
        switch (city){
            case "киев" :
            case "днепр" :
            case "харьков" :
            case "одесса" :
            case "львов" :
            case "винница" :
            case "варшава" :
            case "польша" :
            case "николаев" :
            case "запорожье" :
            case "черновцы" :
            case "чернигов" :
            case "ивано-франковск" :
            case "ужгород" :
            case "за_рубежем": city = "-1";
                break;
            case "удаленно": city = "ukraina";
                break;
            case "санкт-петербург" : city = "sankt-peterburg";
                break;
            case "москва" : city = "moskva";
                break;
            case "нижний новгород" : city = "nizhniy_novgorod";
                break;
            case "казань" : city = "kazan";
                break;
            case "екатеринбург" : city = "ekaterinburg";
                break;
            case "краснодар": city = "krasnodar";
                break;
            case "пермь" : city = "perm";
                break;
            case "ростов-на-дону" : city = "rostov-na-donu";
                break;
            case "томск" : city = "tomsk";
                break;
            case "самара" : city = "samara";
                break;
            case "ульяновск" :  city = "ulyanovsk";
                break;
            case "минск": city = "minsk";
                break;
        }
        return city;
    }

    public static String getAddressNofluffjobs(String address) {
        return address.equals("") ? "Польша" : address;
    }

    public static String getAddressDjinni(String address, String address2) {
        for (String s: address2.split(" ")) {
            address = address.replaceAll(s, "").trim();
        }
        address = address.substring(address.indexOf("в") + 1).replaceAll("·", "").trim();
        address = address.replaceAll("remote", "удаленно");
        return address;
    }

    public static String getCorrectLinkedin(String address) {
        String[] addressParts = address.split(",");
        return addressParts.length > 1 && addressParts[0].trim().equalsIgnoreCase(addressParts[1].trim()) ?
                address.substring(address.indexOf(",") + 1).trim() : address;
    }

    public static String getCorrectYandex(String address, Freshen freshen) {
        address = address.equalsIgnoreCase(freshen.getWorkplace()) ? address : freshen.getWorkplace().concat(", ").concat(address).trim();
        if (address.contains(",")) {
            String[] addressParts = address.split(",");
            address = addressParts[0].equalsIgnoreCase(addressParts[1]) ? address.substring(address.indexOf("," + 1)).trim() : address;
        }
        return address;
    }
}
