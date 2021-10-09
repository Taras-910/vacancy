package ua.training.top.util.parser.data;

import ua.training.top.model.Freshen;

public class CorrectAddress {

    public static String getCityGrc(String city){
        switch (city) {
            case "київ", "киев" -> city = "115";
            case "дніпро", "днепр" -> city = "2126";
            case "одеса", "одесса" -> city = "2188";
            case "львів", "львов" -> city = "2180";
            case "харьків", "харьков" -> city = "2206";
            case "россия" -> city = "113";
            case "санкт-петербург" -> city = "2";
            case "москва" -> city = "1";
            case "новосибирск" -> city = "1202";
            case "нижний новгород" -> city = "1679";
            case "казань" -> city = "1624";
            case "екатеринбург" -> city = "1261";
            case "краснодар" -> city = "1438";
            case "пермь" -> city = "1317";
            case "мінськ", "минск" -> city = "1002";
            case "ростов-на-дону" -> city = "1530";
            case "томск" -> city = "1255";
            case "самара" -> city = "1586";
            case "ульяновск" -> city = "1614";
            case "воронеж" -> city = "1844";
            case "ізраїль", "израиль" -> city = "33";
            case "сша" -> city = "85";
            case "германія", "германия" -> city = "27";
            case "швеція", "швеция" -> city = "149";
            case "норвегия", "норвегія" -> city = "207";
            case "польща", "польша" -> city = "74";
            case "україна", "украина" -> city = "5";
        }
        return city.equals("удаленно") ? "schedule=remote" : "area=".concat(city);
    }

    public static String getCityHabr(String city){
        switch (city) {
            case "київ", "киев" -> city = "908";
            case "санкт-петербург" -> city = "679";
            case "москва" -> city = "678";
            case "новосибирск" -> city = "717";
            case "нижний новгород" -> city = "715";
            case "казань" -> city = "698";
            case "екатеринбург" -> city = "693";
            case "краснодар" -> city = "707";
            case "пермь" -> city = "722";
            case "минск", "мінськ" -> city = "713";
            case "ростов-на-дону" -> city = "726";
            case "томск" -> city = "736";
            case "самара" -> city = "728";
            case "ульяновск" -> city = "739";
            case "воронеж" -> city = "692";
            case "удаленно" -> city = "удаленно";
            default -> city = "-1";
        }
        return city;
    }

    public static String getCityJobs(String city){
        switch (city) {
            case "київ", "киев" -> city = "Київ";
            case "дніпро", "днепр" -> city = "Дніпро";
            case "харків", "харьков" -> city = "Харків";
            case "одеса", "одесса" -> city = "Одеса";
            case "львів", "львов" -> city = "Львів";
            case "миколаїв", "николаев" -> city = "Миколаїв";
            case "вінниця", "винница" -> city = "Вінниця";
            case "запоріжжя", "запорожье" -> city = "Запоріжжя";
            case "черкаси", "черкассы" -> city = "Черкаси";
            case "суми", "сумы" -> city = "Суми";
            case "чернігів", "чернигов" -> city = "Чернігів";
            case "івано-франківськ", "ивано-франковск" -> city = "Івано-Франківськ";
            case "ужгород" -> city = "Ужгород";
            case "санкт-петербург", "москва", "новосибирск", "нижний новгород", "казань", "екатеринбург", "краснодар",
                    "пермь", "ростов-на-дону", "томск", "самара", "ульяновск" -> city = "за_рубежем";
        }
        return city;
    }

    public static String getToLinkedin(String address) {
        String[] addressParts = address.split(",");
        return addressParts.length > 1 && addressParts[0].trim().equalsIgnoreCase(addressParts[1].trim()) ?
                address.substring(address.indexOf(",") + 1).trim() : address;
    }

    public static String getCityLinkedin(String city){
        switch (city) {
            case "україна", "украина" -> city = "102264497";
            case "київ", "киев" -> city = "90010216";
            case "дніпро", "днепр" -> city = "90010219";
            case "одеса", "одесса" -> city = "90010220";
            case "львів", "львов" -> city = "104983263";
            case "харьків", "харьков" -> city = "90010217";
            case "вінниця", "винница" -> city = "90010222";
            case "ужгород" -> city = "90010233";
            case "івано-франківск", "ивано-франковск" -> city = "109800298";
            case "полтава" -> city = "102507522";
            case "запоріжжя", "запорожье" -> city = "104184784";
            case "ізраїль", "израиль" -> city = "101620260";
            case "швейцарія", "швейцария" -> city = "106693272";
            case "франція", "франция" -> city = "105015875";
            case "італія", "италия" -> city = "103350119";
            case "сінгапур", "сингапур" -> city = "102454443";
            case "англія", "англия" -> city = "101165590";
            case "оаэ", "оае" -> city = "104305776";
            case "канада" -> city = "101174742";
            case "польща", "польша" -> city = "105072130";
            case "німеччина", "германия" -> city = "101282230";
            case "чехія", "чехия" -> city = "104508036";
            case "швеція", "швеция" -> city = "105117694";
            case "фінляндія", "финляндия" -> city = "100456013";
            case "россия" -> city = "101728296";
            case "санкт-петербург" -> city = "90010184";
            case "москва" -> city = "111154941";
            case "новосибирск" -> city = "90010146";
            case "нижний новгород" -> city = "104043205";
            case "казань" -> city = "101631519";
            case "екатеринбург" -> city = "90010185";
            case "краснодар" -> city = "106273043";
            case "пермь" -> city = "103472036";
            case "мінськ", "минск" -> city = "105415465";
            case "ростов-на-дону" -> city = "102450862";
            case "томск" -> city = "90010159";
            case "самара" -> city = "106843614";
            case "ульяновск" -> city = "107078222";
            case "воронеж" -> city = "102084685";
            default -> city = "103644278";
        }
        return city;
    }

    public static String getToNofluffjobs(String address) {
        return address.equals("") ? "Польша" : address;
    }

    public static String getCityNofluff(String city) {
        switch (city) {
            case "украина", "україна", "киев", "київ", "днепр", "дніпро", "харьков", "харків", "одесса", "одеса",
                    "львов", "львів", "николаев", "миколаїв", "запорожье", "запоріжжя", "черновцы", "чорновці",
                    "чернигов", "чернігів", "ивано-франковск", "івано-франківськ", "ужгород", "санкт-петербург",
                    "москва", "новосибирск", "нижний новгород", "казань", "екатеринбург", "краснодар", "пермь",
                    "ростов-на-дону", "томск", "самара", "минск", "мінськ", "ульяновск" -> city = "-1";
            case "варшава", "warszawa" -> city = "warszawa/";
            case "krakow", "краков" -> city = "krakow/";
            case "wroclaw", "вроцлав" -> city = "wroclaw/";
            case "gdansk", "гданськ", "гданск" -> city = "gdansk/";
            case "poznan", "познань" -> city = "poznan/";
            case "katowice", "катовіце", "катовице" -> city = "katowice/";
            case "lodz", "лодзь" -> city = "lodz/";
            default -> city = "praca-zdalna/";
        }
        return city;
    }

    public static String getCityRabota(String city) {
        switch (city) {
            case "київ", "киев" -> city = "киев";
            case "дніпро", "днепр" -> city = "днепр";
            case "одеса", "одесса" -> city = "одесса";
            case "львів", "львов" -> city = "львов";
            case "харьків", "харьков" -> city = "харьков";
            case "ізраїль", "израиль", "швейцарія", "швейцария", "франція", "франция", "італія", "италия", "сінгапур",
                    "сингапур", "англія", "англия", "емірати", "эмираты", "канада", "польща", "польша", "німеччина",
                    "германия", "чехія", "чехия", "швеція", "швеция", "фінляндія", "финляндия", "россия", "за_рубежем"
                    -> city = "другие_страны";
            default -> city = "украина";
        }
        return city;
    }

    public static String getCityJooble(String city){
        switch (city) {
            case "київ", "киев" -> city = "Київ";
            case "дніпро", "днепр" -> city = "Дніпро";
            case "харків", "харьков" -> city = "Харків";
            case "одеса", "одесса" -> city = "Одеса";
            case "львів", "львов" -> city = "Львів";
            case "миколаїв", "николаев" -> city = "Миколаїв";
            case "вінниця", "винница" -> city = "Вінниця";
            case "запоріжжя", "запорожье" -> city = "Запоріжжя";
            case "чорновці", "черновцы" -> city = "Чорновці";
            case "чернігів", "чернигов" -> city = "Чернігів";
            case "івано-франківськ", "ивано-франковск" -> city = "Івано-Франківськ";
            case "ужгород" -> city = "Ужгород";
            case "санкт-петербург", "москва", "новосибирск", "нижний новгород", "казань", "екатеринбург", "краснодар",
                    "пермь", "ростов-на-дону", "томск", "самара", "ульяновск" -> city = "-1";
            case "мінськ", "минск" -> city = "Мінськ%2C%20Білорусь";
            case "варшава" -> city = "Варшава%2C%20Польща";
            case "краків", "краков" -> city = "Краків%2C%20Польща";
            case "удаленно" -> city = "&loc=2";
            case "за_рубежем" -> city = "за%20кордоном";
        }
        return city.equals("удаленно") ? city : "&rgns=".concat(city);
    }

    public static String getCityWork(String workspace) {
        return workspace.equals("remote") || workspace.equals("ua") ? "" : "-".concat(workspace);
    }

    public static String getCityYandex(String city){
        switch (city) {
            case "удаленно" -> city = "remote";
            case "киев","київ","kiev" -> city = "kiev";
            case "санкт-петербург" -> city = "sankt-peterburg";
            case "москва" -> city = "moskva";
            case "нижний новгород" -> city = "nizhniy_novgorod";
            case "казань" -> city = "kazan";
            case "екатеринбург" -> city = "ekaterinburg";
            case "краснодар" -> city = "krasnodar";
            case "пермь" -> city = "perm";
            case "ростов-на-дону" -> city = "rostov-na-donu";
            case "томск" -> city = "tomsk";
            case "самара" -> city = "samara";
            case "ульяновск" -> city = "ulyanovsk";
            case "минск" -> city = "minsk";
            default -> city = "-1";
        }
        return city;
    }

    public static String getToYandex(String address, Freshen freshen) {
        address = address.equalsIgnoreCase(freshen.getWorkplace()) ? address : freshen.getWorkplace().concat(", ").concat(address).trim();
        if (address.contains(",")) {
            String[] addressParts = address.split(",");
            address = addressParts[0].equalsIgnoreCase(addressParts[1]) ? address.substring(address.indexOf("," + 1)).trim() : address;
        }
        return address;
    }

    public static String getCorrectAddress(String city){
        city = city.contains("Агломерация") ? city.replace("Агломерация", "агломерация") : city;
        city = city.contains("VIP") ? city.substring(city.indexOf("P") + 3).trim() : city;
        city = city.contains("віддалено") ? city.replaceAll("віддалено", "удаленно") : city;
        switch (city.toLowerCase()) {
            case "київ", "киев" -> city = "Киев";
            case "дніпро", "днепр" -> city = "Днепр";
            case "харків", "харьков" -> city = "Харьков";
            case "одеса", "одесса" -> city = "Одесса";
            case "львів", "львов" -> city = "Львов";
            case "миколаїв", "николаев" -> city = "Николаев";
            case "вінниця", "винница" -> city = "Винница";
            case "запоріжжя", "запорожье" -> city = "Запорожье";
            case "чорновці", "черновцы" -> city = "Черновцы";
            case "чернігів", "чернигов" -> city = "Чернигов";
            case "івано-франківськ", "ивано-франковск" -> city = "Ивано-франковск";
            case "ужгород" -> city = "Ужгород";
            case "мінськ", "минск" -> city = "Минск";
            case "другие страны" -> city = "за_рубежем";
            case "удаленно", "віддалена робота", "no location" -> city = "удаленно";
            default -> city = city == null || city.equals("") || city.length() < 2 ?
                    city : city.substring(0, 1).toUpperCase().concat(city.substring(1));
        }
        return city;
    }

    public static String getTranslated(String city){
        switch (city) {
            case "україна", "украина" -> city = "ua";
            case "київ", "киев" -> city = "kyiv";
            case "дніпро", "днепр" -> city = "dnipro";
            case "харків", "харьков" -> city = "kharkiv";
            case "одеса", "одесса" -> city = "odesa";
            case "львів", "львов" -> city = "lviv";
            case "миколаїв", "николаев" -> city = "mykolaiv";
            case "вінниця", "винница" -> city = "vinnitsia";
            case "запоріжжя", "запорожье" -> city = "zaporizhzhya";
            case "чорновці", "черновцы" -> city = "chernivtsi";
            case "чернігів", "чернигов" -> city = "chernigiv";
            case "івано-франківськ", "ивано-франковск" -> city = "ivano-frankivsk";
            case "ужгород" -> city = "uzhgorod";
            case "удаленно" -> city = "remote";
            case "санкт-петербург", "москва", "новосибирск", "нижний новгород", "казань", "екатеринбург", "краснодар",
                    "пермь", "минск", "ростов-на-дону", "томск", "самара", "ульяновск", "воронеж" -> city = "-1";
            default -> city = "other";
        }
        return city;
    }

    public static boolean isMatchesRu(String city){
        return switch (city) {
            case "россия", "санкт-петербург", "москва", "новосибирск", "нижний новгород", "казань", "екатеринбург",
                    "краснодар", "пермь", "ростов-на-дону", "томск", "самара", "ульяновск", "минск", "воронеж" -> false;
            default -> true;
        };
    }
}
