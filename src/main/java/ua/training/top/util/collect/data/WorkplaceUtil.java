package ua.training.top.util.collect.data;

import static ua.training.top.util.collect.data.DataUtil.*;

public class WorkplaceUtil {

    public static String getDjinni(String city) {
        String workplace =  isMatch(citiesUA, city) ? "UKR" : isMatch(citiesPL, city) ? "POL" :
                isMatch(citiesDe, city) ? "DEU" : city.equals("all") ? "eu" : city.equals("remote") ? "remote" :  "other";
        return getJoin("?",city.equals("remote") ? "employment=" : "region=",workplace);
    }

    public static String getITJobs(String workplace) {
        return switch (workplace) {
            case "all", "canada", "канада", "другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном" -> "all";
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "remote";
            case "манитоба", "манітоба", "manitoba" -> "&location=Manitoba&location-id=MB&location-type=2";
            case "ontario", "онтарио", "онтаріо" -> "&location=Ontario&location-id=ON&location-type=2";
            case "ottawa", "оттава" -> "&location=Ottawa%2C+ON&location-id=315580&location-type=1";
            case "альберта", "alberta" -> "&location=Alberta&location-id=AB&location-type=2";
            case "quebec", "квебек" -> "&location=Quebec%2C+QC&location-id=117253&location-type=1";
            case "toronto", "торонто" -> "&location=Toronto%2C+ON&location-id=481104&location-type=1";
            case "montréal", "монреаль" -> "&location=Montreal%2C+QC&location-id=182156&location-type=1";
            case "vancouver", "ванкувер" -> "&location=Vancouver%2C+BC&location-id=775867&location-type=1";
            case "calgary", "калгарі", "калгари" -> "&location=Calgary%2C+AB&location-id=647636&location-type=1";
            case "brampton", "брэмптон", "бремптон" -> "&location=Brampton%2C+ON&location-id=420051&location-type=1";
            case "edmonton", "едмонтон", "эдмонтон" -> "&location=Edmonton%2C+AB&location-id=673364&location-type=1";
            case "victoria", "виктория", "вікторія" -> "&location=Victoria%2C+BC&location-id=806417&location-type=1";
            case "winnipeg", "виннипег", "вінніпег" -> "&location=Winnipeg%2C+MB&location-id=592959&location-type=1";
            case "hamilton", "гамильтон", "гамільтон" -> "&location=Hamilton%2C+ON&location-id=440913&location-type=1";
            case "mississauga", "миссиссога", "міссісога" -> "&location=Mississauga%2C+ON&location-id=398125&location-type=1";
            case "британська колумбія", "британская колумбия", "british columbia" -> "&location=British+Columbia&location-id=BC&location-type=2";
            default -> "";
        };
    }

    public static String getJobsBG(String city){
        return  switch (city) {
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "&is_distance_job=1";
            case "софия", "софія", "sofia" -> "&location_sid=1";
            case "пловдив", "пловдів", "plovdiv" -> "&location_sid=2";
            case "бургас", "burgas" -> "&location_sid=4";
            case "варна", "varna" -> "&location_sid=3";
            case "русе", "ruse" -> "&location_sid=19";
            case "плевен", "pleven" -> "&location_sid=18";
            case "шумен", "shumen" -> "&location_sid=23";
            case "ямполь", "yampol" -> "&location_sid=24";
            case "добрич", "dobrich" -> "&location_sid=11";
            case "банско", "bansko" -> "&location_sid=47";
            case "силистра", "сілістра", "silistra" -> "&location_sid=39";
            case "ловеч", "lovech" -> "&location_sid=14";
            case "смолян", "smolyan" -> "&location_sid=46";
            case "благоевград", "blagoevgrad" -> "&location_sid=6";
            default -> ""; // Болгарія, foreign, all
        };
    }

    public static String getJobs(String city){
        return  switch (city) {
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "remote";
            case "київ", "киев", "kyiv", "kiev" -> "Київ";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "Дніпро";
            case "харків", "харьков", "kharkiv" -> "Харків";
            case "одеса", "одесса", "odessa" -> "Одеса";
            case "львів", "львов", "lviv" -> "Львів";
            case "миколаїв", "николаев", "mykolaiv" -> "Миколаїв";
            case "вінниця", "винница", "vinnitsia" -> "Вінниця";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "Запоріжжя";
            case "черкаси", "черкассы" -> "Черкаси";
            case "суми", "сумы", "sumi" -> "Суми";
            case "чернігів", "чернигов", "chernigiv" -> "Чернігів";
            case "івано-франківськ", "ивано-франковск" -> "Івано-Франківськ";
            case "ужгород", "uzhgorod" -> "Ужгород";
            default -> "all"; // Украина, all
        };
    }

    /*public static String getLinkedin(String city){
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
            case "мінськ", "минск" -> "&location=Минск%2C%20Минск%2C%20Республика%20Беларусь&geoId=105415465";
            default -> "&location=Украина&geoId=102264497"; //Украина
        };
    }*/
    public static String getLinkedin(String city){
        return switch (city) {
            case "київ", "киев", "kyiv", "kiev" -> "&geoId=104035893";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "&geoId=103663309";
            case "одеса", "одесса", "odessa" -> "&geoId=100182723";
            case "львів", "львов", "lviv" -> "&geoId=104983263";
            case "харьків", "харьков", "kharkiv" -> "&geoId=103352426";
            case "вінниця", "винница", "vinnitsia" -> "&geoId=106030501";
            case "ужгород", "uzhgorod" -> "&location=&geoId=106974374";
            case "івано-франківск", "ивано-франковск" -> "&geoId=";
            case "полтава", "poltava" -> "&geoId=102507522";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "geoId=104184784";
            case "черкаси", "черкассы" -> "&geoId=104320082";
            case "тернопіль", "тернополь" -> "&geoId=101854836";
            case "чернігів", "ченигов", "chernigiv" -> "&geoId=100735342";
            case "сша", "usa", "united states" -> "&geoId=103644278";
            case "ізраїль", "израиль", "israel" -> "&geoId=101620260";
            case "швейцарія", "швейцария", "switzerland" -> "&geoId=100017800";
            case "франція", "франция", "france" -> "&geoId=105015875";
            case "італія", "италия", "italy" -> "&geoId=103350119";
            case "сінгапур", "сингапур", "singapore" -> "&geoId=102454443";
            case "англія", "англия", "england", "uk", "united kingdom" -> "&geoId=90009496";
            case "оаэ", "оае", "эмираты", "emirates"  -> "&geoId=104305776";
            case "канада", "canada" -> "&geoId=101174742";
            case "торонто","toronto" -> "&geoId=90009551";
            case "брамптон", "brampton" -> "&geoId=104669182";
            case "ванкувер","vancouver" -> "&geoId=90009553";
            case "монреаль", "montreal", "montréal" -> "&geoId=90009540";
            case "виктория", "вікторія", "victoria" -> "&geoId=100346955";
            case "оттава", "ottawa" -> "&geoId=100346955";
            case "гамильтон", "hamilton" -> "&geoId=104444106";
            case "виннипег", "winnipeg" -> "&geoId=101213860";
            case "польща", "польша", "poland", "pol" -> "&geoId=105072130";
            case "варшава", "warszawa" -> "&geoId=105076658";
            case "німеччина", "germany", "германия" -> "&geoId=101282230";
            case "czechia","чехія", "чехия" -> "&geoId=104508036";
            case "швеція", "швеция", "sweden" -> "&geoId=105117694";
            case "фінляндія", "финляндия", "finland" -> "&geoId=100456013";
            case "черногория" -> "&geoId=100733275";
            case "мінськ", "минск" -> "&geoId=105415465";
            default -> "&geoId=102264497"; //Украина
        };
    }

    public static String getNofluff(String city) {
        return switch (city) {
            case "польша", "польща", "poland", "polski", "all", "foreign" -> "";
            case "варшава", "warszawa" -> "warszawa/";
            case "krakow", "краков", "краків" -> "krakow/";
            case "wroclaw", "вроцлав" -> "wroclaw/";
            case "gdansk", "гданськ", "гданск" -> "gdansk/";
            case "poznan", "познань" -> "poznan/";
            case "katowice", "катовіце", "катовице" -> "katowice/";
            case "lodz", "лодзь" -> "lodz/";
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "praca-zdalna/";
            default -> !isMatch(citiesPL, city) ? "-1" : "";
        };
    }

    public static String getRabota(String workplace) {
        return switch (workplace) {
            case "remote", "relocate", "удаленно", "віддалено", "all", "украина", "україна", "ukraine" -> "украина";
            case "київ", "киев", "kyiv", "kiev" -> "киев";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "днепр";
            case "одеса", "одесса", "odessa" -> "одесса";
            case "львів", "львов", "lviv" -> "львов";
            case "харьків", "харьков", "kharkiv" -> "харьков";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "запорожье";
            case "миколаїв", "николаев", "mykolaiv" -> "николаев";
            case "чорновці", "черновцы", "chernivtsi" -> "черновцы";
            case "чернігів", "чернигов", "chernigiv" -> "чернигов";
            case "вінниця", "винница" -> "винница";
            default -> !isMatch(citiesUA, workplace) || isMatch(foreignAria, workplace) ? "другие_страны" : "украина";
        };
    }

    public static String getIndeed(String workplace) {
        return switch (workplace) {
            case "украина", "україна", "ukraine", "all" -> "&l=Украина";
            case "remote", "relocate", "удаленно", "віддалено", "віддалена робота" -> "&rbl=Удаленно&jlid=f00b7fa4b055cc00";
            case "київ", "киев", "kyiv", "kiev" -> "&rbl=Киев&jlid=e9ab1a23f8e591f1";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "&rbl=Днепр,+Днепропетровская+область&jlid=030c410a355d8014";
            case "одеса", "одесса", "odessa" -> "&rbl=Одесса&jlid=240fe96bd3c6e402";
            case "львів", "львов", "lviv" -> "&rbl=Львов&jlid=6ea57808cf02b292";
            case "харьків", "харьков", "kharkiv" -> "&rbl=Харьков&jlid=6fb70c8a2ab37b1f";
            default -> "-1";
        };
    }

    public static String getCad(String workplace) {
        return switch (workplace) {
            case "canada", "all", "канада" -> "Canada";
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "Remote";
            case "toronto", "торонто" -> "Toronto%2C+ON";
            case "calgary", "калгарі", "калгари" -> "Calgary%2C+AB";
            case "ontario", "онтарио", "онтаріо" -> "Ontario";
            case "edmonton", "едмонтон", "эдмонтон" -> "Edmonton%2C+AB";
            case "vancouver", "ванкувер" -> "Vancouver%2C+BC";
            case "brampton", "брэмптон", "бремптон" -> "Brampton%2C+ON";
            case "ottawa", "оттава" -> "Ottawa%2C+ON";
            case "mississauga", "миссиссога", "міссісога" -> "Mississauga%2C+ON";
            case "британська колумбія", "британская колумбия", "british columbia" -> "British+Columbia";
            case "альберта", "alberta" -> "Alberta";
            case "манитоба", "манітоба", "manitoba" -> "Manitoba";
            default -> "-1";
        };
    }

    public static String getJooble(String workplace){
        String city = workplace;
        switch (city) {
            case "київ", "киев", "kyiv", "kiev" -> city = "Київ";
            case "дніпро", "днепр", "dnipro", "dnepr" -> city = "Дніпро";
            case "харків", "харьков", "kharkiv" -> city = "Харків";
            case "одеса", "одесса", "odessa" -> city = "Одеса";
            case "львів", "львов", "lviv" -> city = "Львів";
            case "миколаїв", "николаев", "mykolaiv" -> city = "Миколаїв";
            case "вінниця", "винница", "vinnitsia" -> city = "Вінниця";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> city = "Запоріжжя";
            case "чорновці", "черновцы", "chernivtsi" -> city = "Чорновці";
            case "чернігів", "чернигов", "chernigiv" -> city = "Чернігів";
            case "івано-франківськ", "ивано-франковск" -> city = "Івано-Франківськ";
            case "ужгород", "uzhgorod" -> city = "Ужгород";
            case "мінськ", "минск" -> city = "Мінськ%2C%20Білорусь";
            case "варшава" -> city = "Варшава%2C%20Польща";
            case "краків", "краков" -> city = "Краків%2C%20Польща";
            case "германия", "німеччина", "germany" -> city = "Німеччина";
            case "польша", "польща", "poland" -> city = "Польща";
            case "канада", "canada" -> city = "Канада";
            case "другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном" -> city = "за%20кордоном";
            default -> city = workplace;
        }
        return isMatch(remoteAria, workplace) || workplace.equals("all") ? "" : getJoin("&rgns=",city);
    }

    public static String getWork(String workplace) {
        return switch (workplace) {
            case "україна", "украина", "all", "remote", "relocate", "релокейт", "удаленно", "віддалено" -> "";
            case "київ", "киев", "kiev" -> "-kyiv";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "-zaporizhzhya";
            case "миколаїв", "николаев", "mykolaiv" -> "-mykolaiv";
            case "чорновці", "черновцы", "chernivtsi" -> "-chernivtsi";
            case "чернігів", "чернигов", "chernigiv" -> "-chernigiv";
            case "вінниця", "винница", "vinnitsia" -> "-vinnitsia";
            case "харків", "харьков", "kharkiv" -> "-kharkiv";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "-dnipro";
            case "одеса", "одесса", "odessa" -> "-odesa";
            case "львів", "львов", "lviv" -> "-lviv";
            case "ужгород", "uzhgorod" -> "-uzhgorod";
            case "івано-франківськ", "ивано-франковск" -> "-ivano-frankivsk";
            default -> "-other"; //foreign
        };
    }
}
