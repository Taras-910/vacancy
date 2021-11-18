package ua.training.top.util.collect.data;

import static ua.training.top.util.collect.data.DataUtil.isCityPl;
import static ua.training.top.util.collect.data.DataUtil.isCityUA;

public class WorkplaceUtil {

    public static String getGrc(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "115";
            case "дніпро", "днепр" -> "2126";
            case "одеса", "одесса" -> "2188";
            case "львів", "львов" -> "2180";
            case "россия" -> "113";
            case "москва" -> "1";
            case "казань" -> "1624";
            case "пермь" -> "1317";
            case "томск" -> "1255";
            case "самара" -> "1586";
            case "воронеж" -> "1844";
            case "краснодар" -> "1438";
            case "ульяновск" -> "1614";
            case "харьків", "харьков" -> "2206";
            case "санкт-петербург" -> "2";
            case "нижний новгород" -> "1679";
            case "мінськ", "минск" -> "1002";
            case "ростов-на-дону" -> "1530";
            case "новосибирск" -> "1202";
            case "екатеринбург" -> "1261";
            case "германія", "германия" -> "27";
            case "норвегия", "норвегія" -> "207";
            case "україна", "украина" -> "5";
            case "ізраїль", "израиль" -> "33";
            case "швеція", "швеция" -> "149";
            case "польща", "польша" -> "74";
            case "сша" -> "85";
            case "all" -> "all";
            default -> workplace.equals("remote") ? "&schedule=remote" : "&area=".concat(workplace);
        };
    }

    public static String getHabr(String workplace) {
        return switch (workplace) {
            case "київ", "киев" -> "908";
            case "новосибирск" -> "717";
            case "санкт-петербург" -> "679";
            case "нижний новгород" -> "715";
            case "минск", "мінськ" -> "713";
            case "ростов-на-дону" -> "726";
            case "екатеринбург" -> "693";
            case "краснодар" -> "707";
            case "ульяновск" -> "739";
            case "воронеж" -> "692";
            case "москва" -> "678";
            case "казань" -> "698";
            case "самара" -> "728";
            case "пермь" -> "722";
            case "томск" -> "736";
            case "all" -> "all";
            case "remote" -> "remote";
            default -> "-1";
        };
    }

    public static String getCareer(String city){
        return switch (city) {
            case "україна", "украина", "all", "remote" -> "ukraine";
            case "київ", "киев", "kiev", "kyiv" -> "kiev";
            case "запоріжжя", "запорожье" -> "zaporozhe";
            case "миколаїв", "николаев" -> "nikolaev";
            case "чорновці", "черновцы" -> "chernovcy";
            case "чернігів", "чернигов" -> "chernigov";
            case "вінниця", "винница" -> "vinnica";
            case "харків", "харьков" -> "harkov";
            case "дніпро", "днепр" -> "dnepr";
            case "одеса", "одесса" -> "odessa";
            case "львів", "львов" -> "lvov";
            case "ужгород" -> "uzhgorod";
            case "івано-франківськ", "ивано-франковск" -> "ivano-frankovsk";
            case "санкт-петербург" -> "spb";
            case "москва" -> "msk";
            case "новосибирск" -> "novosibirsk";
            case "нижний новгород" -> "nizhniy_novgorod";
            case "казань" -> "kazan";
            case "екатеринбург" -> "ekaterinburg";
            case "краснодар" -> "krasnodar";
            case "пермь" -> "perm";
            case "ростов-на-дону" -> "rostov";
            case "томск" -> "tomsk";
            case "омск" -> "omsk";
            case "самара" -> "samara";
            case "россия" -> "";
            case "минск" -> "minsk";
            default -> "-1";
        };
    }

    public static String getJobs(String city){
        return  switch (city) {
            case "remote" -> "remote";
            case "київ", "киев" -> "Київ";
            case "дніпро", "днепр" -> "Дніпро";
            case "харків", "харьков" -> "Харків";
            case "одеса", "одесса" -> "Одеса";
            case "львів", "львов" -> "Львів";
            case "миколаїв", "николаев" -> "Миколаїв";
            case "вінниця", "винница" -> "Вінниця";
            case "запоріжжя", "запорожье" -> "Запоріжжя";
            case "черкаси", "черкассы" -> "Черкаси";
            case "суми", "сумы" -> "Суми";
            case "чернігів", "чернигов" -> "Чернігів";
            case "івано-франківськ", "ивано-франковск" -> "Івано-Франківськ";
            case "ужгород" -> "Ужгород";
            case "санкт-петербург", "москва", "новосибирск", "нижний новгород", "казань", "екатеринбург", "краснодар",
                    "пермь", "ростов-на-дону", "томск", "самара", "ульяновск", "foreign" -> "relocation";
            default -> "all"; // Украина, all
        };
    }

    public static String getLinkedin(String city){
        return switch (city) {
            case "київ", "киев" -> "&location=Киев%2C%20Киев%2C%20Украина&geoId=104035893";
            case "дніпро", "днепр" -> "&location=Днепропетровск%2C%20Днепропетровская%20область%2C%20Украина&geoId=103663309";
            case "одеса", "одесса" -> "&location=Одесса%2C%20Одесская%20область%2C%20Украина&geoId=100182723";
            case "львів", "львов" -> "&location=Львов%2C%20Львовская%20область%2C%20Украина&geoId=104983263";
            case "харьків", "харьков" -> "&location=Харьков%2C%20Харьковская%20область%2C%20Украина&geoId=103352426";
            case "remote" -> "&location=Украина&locationId=&geoId=102264497";
            case "вінниця", "винница" -> "&location=Винница%2C%20Винницкая%20область%2C%20Украина&geoId=106030501";
            case "ужгород" -> "&location=Ужгород%2C%20Закарпатская%20область%2C%20Украина&geoId=106974374";
            case "івано-франківск", "ивано-франковск" -> "&location=ивано-франковск&geoId=";
            case "полтава" -> "&location=Полтава%2C%20Полтавская%20область%2C%20Украина&geoId=102507522";
            case "запоріжжя", "запорожье" -> "&location=Запорожье%2C%20Запорожская%20область%2C%20Украина&geoId=104184784";
            case "черкаси", "черкассы" -> "&location=Черкассы%2C%20Черкасская%20область%2C%20Украина&geoId=104320082";
            case "тернопіль", "тернополь" -> "&location=Тернополь%2C%20Тернопольская%20область%2C%20Украина&geoId=101854836";
            case "чернігів", "ченигов" -> "&location=Чернигов%2C%20Черниговская%20область%2C%20Украина&geoId=100735342";
            case "сша" -> "&location=Соединенные%20Штаты%20Америки&geoId=103644278";
            case "ізраїль", "израиль" -> "&location=Израиль&geoId=101620260";
            case "швейцарія", "швейцария" -> "&location=Саксонская+Швейцария%2C+Саксония%2C+Германия&geoId=100017800";
            case "франція", "франция" -> "&location=Франция&geoId=105015875";
            case "італія", "италия" -> "&location=Италия&geoId=103350119";
            case "сінгапур", "сингапур" -> "&location=Сингапур&geoId=102454443";
            case "англія", "англия" -> "&location=Агломерация%20Лондона%2C%20Великобритания&geoId=90009496";
            case "оаэ", "оае" -> "&location=Объединенные%20Арабские%20Эмираты&geoId=104305776";
            case "канада" -> "&location=Канада&geoId=101174742";
            case "польща", "польша" -> "&location=Польша&geoId=105072130";
            case "варшава" -> "&location=Варшава%2C%20Мазовецкое%20воеводство%2C%20Польша&geoId=105076658";
            case "німеччина", "германия" -> "&location=Германия&geoId=101282230";
            case "чехія", "чехия" -> "&location=Чехия&geoId=104508036";
            case "швеція", "швеция" -> "&location=Швеция&geoId=105117694";
            case "фінляндія", "финляндия" -> "&location=Финляндия&geoId=100456013";
            case "черногория" -> "&location=Черногория&geoId=100733275";
            case "россия" -> "&location=Россия&geoId=101728296";
            case "санкт-петербург" -> "&location=Санкт-Петербург%2C%20Санкт-Петербург%2C%20Россия&geoId=105830331&";
            case "москва" -> "&location=Москва%2C%20Москва%2C%20Россия&geoId=111154941";
            case "новосибирск" -> "&location=Новосибирск%2C%20Новосибирская%20область%2C%20Россия&geoId=108315007";
            case "нижний новгород" -> "&location=Нижний%20Новгород%2C%20Нижегородская%20область%2C%20Россия&geoId=104043205";
            case "казань" -> "&location=Казань%2C%20Республика%20Татарстан%2C%20Россия&geoId=101631519";
            case "екатеринбург" -> "&location=Екатеринбург%2C%20Свердловская%20область%2C%20Россия&geoId=100367933";
            case "краснодар" -> "&location=Краснодар%2C%20Краснодарский%20край%2C%20Россия&geoId=106273043";
            case "пермь" -> "&location=Пермь%2C%20Пермский%20край%2C%20Россия&geoId=103472036";
            case "мінськ", "минск" -> "&location=Минск%2C%20Минск%2C%20Республика%20Беларусь&geoId=105415465";
            case "ростов-на-дону" -> "&location=Ростов-на-Дону%2C%20Ростовская%20область%2C%20Россия&geoId=102450862";
            case "томск" -> "&location=Томск%2C%20Томская%20область%2C%20Россия&geoId=102719050";
            case "самара" -> "&location=Самара%2C%20Самарская%20область%2C%20Россия&geoId=106843614";
            case "ульяновск" -> "&location=Ульяновск%2C%20Ульяновская%20область%2C%20Россия&geoId=107078222";
            case "воронеж" -> "&location=Воронеж%2C%20Воронежская%20область%2C%20Россия&geoId=102084685";
            default -> "&location=Украина&geoId=102264497"; //Украина
        };
    }

    public static String getNofluff(String city) {
        return switch (city) {
            case "польша", "poland", "polski", "all", "foreign" -> "";
            case "варшава", "warszawa" -> "warszawa/";
            case "krakow", "краков" -> "krakow/";
            case "wroclaw", "вроцлав" -> "wroclaw/";
            case "gdansk", "гданськ", "гданск" -> "gdansk/";
            case "poznan", "познань" -> "poznan/";
            case "katowice", "катовіце", "катовице" -> "katowice/";
            case "lodz", "лодзь" -> "lodz/";
            case "remote" -> "praca-zdalna/";
            default -> !isCityPl(city) ? "-1" : "";
        };
    }

    public static String getRabota(String workplace) {
        return switch (workplace) {
            case "remote", "all", "украина", "україна" -> "украина";
            case "київ", "киев" -> "киев";
            case "дніпро", "днепр" -> "днепр";
            case "одеса", "одесса" -> "одесса";
            case "львів", "львов" -> "львов";
            case "харьків", "харьков" -> "харьков";
            case "запоріжжя", "запорожье" -> "запорожье";
            case "миколаїв", "николаев" -> "николаев";
            case "чорновці", "черновцы" -> "черновцы";
            case "чернігів", "чернигов" -> "чернигов";
            case "вінниця", "винница" -> "винница";
            default -> !isCityUA(workplace) || workplace.equals("foreign") ? "другие_страны" : "украина";
        };
    }


    public static String getIndeed(String workplace) {
        return switch (workplace) {
            case "украина", "all" -> "&l=Украина";
            case "remote" -> "&rbl=Удаленно&jlid=f00b7fa4b055cc00";
            case "київ", "киев" -> "&rbl=Киев&jlid=e9ab1a23f8e591f1";
            case "дніпро", "днепр" -> "&rbl=Днепр,+Днепропетровская+область&jlid=030c410a355d8014";
            case "одеса", "одесса" -> "&rbl=Одесса&jlid=240fe96bd3c6e402";
            case "львів", "львов" -> "&rbl=Львов&jlid=6ea57808cf02b292";
            case "харьків", "харьков" -> "&rbl=Харьков&jlid=6fb70c8a2ab37b1f";
            default -> "-1";
        };
    }

    public static String getJooble(String workplace){
        String city = workplace;
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
            case "мінськ", "минск" -> city = "Мінськ%2C%20Білорусь";
            case "варшава" -> city = "Варшава%2C%20Польща";
            case "краків", "краков" -> city = "Краків%2C%20Польща";
            case "foreign" -> city = "за%20кордоном";
            default -> city = workplace;
        }
        return workplace.equals("remote") || workplace.equals("all") ? "" : "&rgns=".concat(city);
    }

    public static String getWork(String workplace) {
        return switch (workplace) {
            case "україна", "украина", "all", "remote" -> "";
            case "київ", "киев", "kiev" -> "-kyiv";
            case "запоріжжя", "запорожье" -> "-zaporizhzhya";
            case "миколаїв", "николаев" -> "-mykolaiv";
            case "чорновці", "черновцы" -> "-chernivtsi";
            case "чернігів", "чернигов" -> "-chernigiv";
            case "вінниця", "винница" -> "-vinnitsia";
            case "харків", "харьков" -> "-kharkiv";
            case "дніпро", "днепр" -> "-dnipro";
            case "одеса", "одесса" -> "-odesa";
            case "львів", "львов" -> "-lviv";
            case "ужгород" -> "-uzhgorod";
            case "івано-франківськ", "ивано-франковск" -> "-ivano-frankivsk";
            default -> "-other"; //foreign
        };
    }
}
