package ua.training.top.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.List.of;
import static ua.training.top.util.collect.data.DataUtil.*;

public class FilterAriaUtil {
    public static Map<String, List<String>> mapAriaFilter;

    static {
        mapAriaFilter = new TreeMap<>();
        FilterAriaUtil fau = new FilterAriaUtil();
        List<String> list = new ArrayList();
        list.addAll(levelAria);
        list.addAll(foreignAria);
        list.addAll(remoteAria);
        list.addAll(uaAria);
        list.addAll(citiesUA);
        list.addAll(plAria);
        list.addAll(citiesPl);
        list.addAll(caAria);
        list.addAll(citiesCa);
        list.addAll(ukAria);
        list.addAll(citiesUK);
        list.addAll(bgAria);
        list.addAll(citiesBg);
        list.addAll(deAria);
        list.addAll(citiesDe);
        list.addAll(ilAria);
        list.addAll(citiesIl);
        list.addAll(usAria);
        list.addAll(citiesUS);
        list.addAll(otherAria);
        mapAriaFilter = list.stream().distinct().collect(Collectors.toMap(s -> s, fau::getAria));
    }

    public List<String> getAria(String text) {
        return switch (text) {
            case "intern", "trainee", "интерн", "стажировка", "internship", "стажер" -> of("intern", "trainee", "интерн", "internship", "стажировка", "стажер", "стажист");
            case "junior", "младший", "без опыта", "обучение" -> of("junior", "младший", "без опыта", "обучение");
            case "middle", "средний", "мідл" -> of("middle", "средний", "мідл");
            case "senior", "старший" -> of("senior", "старший");
            case "expert", "lead", "тимлид", "ведущий", "team lead" -> of("expert", "lead", "team lead", "ведущий", "тимлид");

            case "foreign", "за_рубежем", "за рубежем", "за кордоном", "другие страны" -> of("ізраїль", "израиль",
                    "israel", "швейцарія", "швейцария", "switzerland", "france", "франція", "франция", "italy", "італія",
                    "италия", "турція", "турция", "turkey", "англія", "англия", "england", "uk", "united kingdom",
                    "канада", "canada", "польща", "польша", "poland", "pol", "czechia", "чехія", "чехия", "швеція",
                    "швеция", "sweden", "фінляндія", "финляндия", "finland", "норвегія", "норвегия", "norway", "сінгапур",
                    "singapore", "німеччина", "germany", "германия", "оаэ", "эмираты", "emirates", "австралія",
                    "австралия", "australia", "філіпіни", "филипины", "philippines", "естонія", "эстония", "estonia",
                    "netherlands", "голландия", "нідерланди", "білорусь", "беларусь", "киргизстан", "казахстан",
                    "узбекистан", "молдова", "іран", "иран", "iran", "азербайджан", "армения", "болгария", "bulgaria",
                    "черногория", "сша", "турция", "турція", "turkey", "латвия", "латвія",
                    "latvia", "литва", "lithuania", "дания", "данія", "denmark", "бельгия", "бельгія", "belgium",
                    "словакия", "словакія", "slovakia", "греция", "греція", "greece", "кипр", "кіпр", "cyprus", "другие страны",
                    "foreign", "за_рубежем", "за рубежом", "за кордоном");
            case "remote", "relocate", "релокейт", "удаленно", "віддалено" -> of("remote", "relocate", "релокейт", "удаленно", "віддалено");

            case "ukraine", "україна", "украина", "ua" -> of("ukraine", "україна", "украина", "ua", "kyiv", "kiev",
                    "київ", "киев", "дніпро", "днепр", "dnipro", "харків", "харьков", "kharkiv", "львів", "львов", "lviv",
                    "mykolaiv", "одесса", "odesa", "одеса", "винница", "vinnitsia", "вінниця", "запоріжжя", "запорожье",
                    "zaporizhzhya", "chernivtsi", "чернівці", "черновцы", "чернігів", "чернигов", "chernigiv", "миколаїв",
                    "івано-франківськ", "ужгород", "ивано-франковск", "ivano-frankivsk", "луцк", "луцьк", "lutsk",
                    "кіровоград", "николаев", "жовті-води", "кривой-рог", "кривий-ріг", "желтые-воды", "ужгород",
                    "uzhgorod", "каменец-подольский", "камянець-подільський", "тернопіль", "тернополь", "ternopil",
                    "кропивницкий", "кропивницький", "кировоград", "кіровоград", "kropyvnytskyi", "kirovograd",
                    "черкаси", "черкассы", "полтава", "poltava", "хмельницький", "khmelnitsky", "рівне", "роно", "rivne",
                    "rovno", "житомир", "zhytomyr");
            case "київ", "киев", "kiev", "kyiv" -> of("київ", "киев", "kiev", "kyiv");
            case "харків", "харьков", "kharkiv" -> of("харків", "харьков", "kharkiv");
            case "дніпро", "днепр", "dnipro" -> of("дніпро", "днепр", "dnipro");
            case "одеса", "одесса", "odesa" -> of("одеса", "одесса", "odesa");
            case "львів", "львов", "lviv" -> of("львів", "львов", "lviv");
            case "запоріжжя", "запорожье", "zaporizhzhya" -> of("запоріжжя", "запорожье", "zaporizhzhya");
            case "миколаїв", "николаев", "mykolaiv" -> of("миколаїв", "николаев", "mykolaiv");
            case "чорновці", "черновцы", "chernivtsi" -> of("чорновці", "черновцы", "chernivtsi");
            case "чернігів", "чернигов", "chernigiv" -> of("чернігів", "чернигов", "chernigiv");
            case "вінниця", "винница", "vinnitsia" -> of("вінниця", "винница", "vinnitsia");
            case "ужгород", "uzhgorod" -> of("ужгород", "uzhgorod");
            case "черкаси", "черкассы", "cherkasy", "cherkassy" -> of("черкаси", "черкассы", "cherkasy", "cherkassy");
            case "полтава", "poltava" -> of("полтава", "poltava");
            case "хмельницький", "khmelnitsky" -> of("хмельницький", "khmelnitsky");
            case "рівне", "ровно", "rivne", "rovno" -> of("рівне", "ровно", "rivne", "rovno");
            case "житомир", "zhytomyr" -> of("житомир", "zhytomyr");
            case "луцк", "луцьк" -> of("луцк", "луцьк", "lutsk");
            case "тернопіль", "тернополь", "ternopil" -> of("тернопіль", "тернополь", "ternopil");
            case "івано-франківськ", "ивано-франковск", "ivano-frankivsk" -> of("івано-франківськ", "ивано-франковск", "ivano-frankivsk");

            case "польша", "польща", "poland", "polski", "pol" -> of("польша", "польща", "poland", "polski", "lodz",
                    "варшава", "warszawa", "krakow", "краков", "kraków", "wroclaw", "вроцлав", "gdansk", "гданськ", "pol",
                    "гданск", "poznan", "познань", "poznań", "katowice", "катовіце", "катовице", "лодзь", "gdynia");
            case "варшава", "warszawa" -> of("варшава", "warszawa");
            case "poznan", "познань" -> of("poznan", "познань");
            case "wroclaw", "вроцлав" -> of("wroclaw", "вроцлав");
            case "krakow", "краков", "краків" -> of("krakow", "краков", "краків");
            case "gdansk", "гданськ", "гданск" -> of("gdansk", "гданськ", "гданск");

            case "canada", "канада", "canad", "канад" -> of("canada", "канада", "canad", "канад", "toronto", "торонто",
                    "calgary", "калгарі", "калгари", "ontario", "онтарио", "онтаріо", "edmonton", "едмонтон", "эдмонтон",
                    "vancouver", "ванкувер", "brampton", "брэмптон", "бремптон", "ottawa", "оттава", "mississauga",
                    "миссиссога", "міссісога", "британська колумбія", "британская колумбия", "british columbia",
                    "альберта", "alberta", "манитоба", "манітоба", "manitoba", "montréal", "montreal", "монреаль",
                    "quebec", "квебек", "hamilton", "гамильтон", "гамільтон", "victoria", "виктория", "вікторія",
                    "winnipeg", "виннипег", "вінніпег");
            case "ottawa", "оттава" -> of("ottawa", "оттава");
            case "ontario", "онтарио" -> of("ontario", "онтарио");
            case "торонто", "toronto" -> of("торонто", "toronto");
            case "альберта", "alberta" -> of("альберта", "alberta");
            case "montréal", "монреаль" -> of("montréal", "монреаль");
            case "brampton", "брамптон" -> of("brampton", "брамптон");
            case "winnipeg", "виннипег" -> of("winnipeg", "виннипег");
            case "victoria", "виктория" -> of("victoria", "виктория");
            case "hamilton", "гамильтон" -> of("hamilton", "гамильтон");
            case "vancouver", "ванкувер" -> of("vancouver", "ванкувер");
            case "quebec", "квебек", "québec" -> of("quebec", "квебек", "québec");
            case "calgary", "калгарі", "калгари" -> of("calgary", "калгарі", "калгари");
            case "edmonton", "едмонтон", "эдмонтон" -> of("edmonton", "едмонтон", "эдмонтон");
            case "манитоба", "манітоба", "manitoba" -> of("манитоба", "манітоба", "manitoba");
            case "mississauga", "миссиссога", "міссісога" -> of("mississauga", "миссиссога", "міссісога");
            case "британська колумбія", "британская колумбия", "british columbia" -> of("британська колумбія", "британская колумбия", "british columbia");

            case "bulgaria", "болгария", "болгарія" -> of("bulgaria", "болгария", "болгарія", "софия", "софія", "sofia",
                    "варна", "varna", "пловдив", "пловдів", "plovdiv", "бургас", "burgas", "русе", "rousse", "плевен",
                    "pleven", "шумен", "shumen", "ямполь", "yampol", "добрич", "dobrich", "банско", "bansko", "силистра",
                    "сілістра", "silistra", "ловеч", "lovech", "смолян", "smolyan", "благоевград", "bulgaria", "болгария",
                    "blagoevgrad");
            case "varna", "варна" -> of("varna", "варна");
            case "burgas", "бургас" -> of("burgas", "бургас");
            case "sofia", "софия", "софія" -> of("sofia", "софия", "софія");
            case "благоевград", "blagoevgrad" -> of("благоевград", "blagoevgrad");
            case "добрич", "dobrich" -> of("добрич", "dobrich");
            case "русе", "rousse" -> of("русе", "rousse");
            case "ловеч", "lovech" -> of("ловеч", "lovech");
            case "шумен", "shumen" -> of("шумен", "shumen");
            case "ямполь", "yampol" -> of("ямполь", "yampol");
            case "банско", "bansko" -> of("банско", "bansko");
            case "плевен", "pleven" -> of("плевен", "pleven");
            case "смолян", "smolyan" -> of("смолян", "smolyan");
            case "пловдив", "пловдів", "plovdiv" -> of("пловдив", "пловдів", "plovdiv");
            case "силистра", "сілістра", "silistra" -> of("силистра", "сілістра", "silistra");

            case "англія", "англия", "england", "united kingdom" -> of("англія", "англия", "england", "united kingdom",
                    "st james", "moorgate", "shoreditch", "soho", "southwark", "aldersgate", "lee bank", "westminster",
                    "milton", "clerkenwell", "bristol", "leeds", "birmingham", "glasgow", "london", "manchester", "avon",
                    "yorkshire", "cambridgeshire", "lanarkshire", "hampshire", "berkshire", "nottinghamshire",
                    "yorkshire", "scotland", "cент джеймс", "клеркенвелл", "мургейт", "шордітч", "шордитч", "сохо",
                    "саутворк", "олдерсгейт", "лі бенк", "ли бенк", "бірмінгем", "вестмінстер", "мілтон", "вестминстер",
                    "милтон", "брістоль", "бристоль", "лидс", "бирмингем", "uk", "глазго", "лондон", "манчестер",
                    "ейвон", "йоркшир", "кембріджшир", "кембриджшир", "лідс", "гемпшир", "ланаркшир", "беркшир",
                    "ноттінгемшир", "ноттингемшир", "йоркшир", "шотландія", "шотландия");
            case "london", "лондон" -> of("london", "лондон");
            case "avon", "ейвон" -> of("avon", "ейвон");
            case "soho", "сохо" -> of("soho", "сохо");
            case "glasgow", "глазго" -> of("glasgow", "глазго");
            case "moorgate", "мургейт" -> of("moorgate", "мургейт");
            case "berkshire", "беркшир" -> of("berkshire", "беркшир");
            case "yorkshire", "йоркшир" -> of("yorkshire", "йоркшир");
            case "hampshire", "гемпшир" -> of("hampshire", "гемпшир");
            case "southwark", "саутворк" -> of("southwark", "саутворк");
            case "leeds", "лидс", "лідс" -> of("leeds", "лидс", "лідс");
            case "шотландія", "шотландия" -> of("шотландія", "шотландия");
            case "manchester", "манчестер" -> of("manchester", "манчестер");
            case "lanarkshire", "ланаркшир" -> of("lanarkshire", "ланаркшир");
            case "aldersgate", "олдерсгейт" -> of("aldersgate", "олдерсгейт");
            case "milton", "мілтон", "милтон" -> of("milton", "мілтон", "милтон");
            case "clerkenwell", "клеркенвелл" -> of("clerkenwell", "клеркенвелл");
            case "bristol", "брістоль", "бристоль" -> of("bristol", "брістоль", "бристоль");
            case "shoreditch", "шордітч", "шордитч" -> of("shoreditch", "шордітч", "шордитч");
            case "birmingham", "бірмінгем", "бирмингем" -> of("birmingham", "бірмінгем", "бирмингем");
            case "westminster", "вестмінстер", "вестминстер" -> of("westminster", "вестмінстер", "вестминстер");
            case "cambridgeshire", "кембріджшир", "кембриджшир" -> of("cambridgeshire", "кембріджшир", "кембриджшир");
            case "st james", "cент джеймс", "st_james", "cент_джеймс" -> of("st james", "cент джеймс", "st_james", "cент_джеймс");
            case "nottinghamshire", "ноттінгемшир", "ноттингемшир" -> of("nottinghamshire", "ноттінгемшир", "ноттингемшир");
            case "lee bank", "лі бенк", "ли бенк", "lee_bank", "лі_бенк", "ли_бенк" -> of("lee bank", "лі бенк", "ли бенк", "lee_bank", "лі_бенк", "ли_бенк");

            case "germany", "германия", "німеччина" -> of("germany", "германия", "німеччина", "берлін", "берлин",
                    "berlin", "мангейм", "mannheim", "гамбург", "hamburg", "ганновер", "hanover", "дюссельдорф",
                    "dusseldorf", "мюнхен", "munich", "франкфурт-на-майне", "frankfurt am main", "karlsruhe", "карлсруе",
                    "кёльн", "köln", "штутгарт", "stuttgart", "дюссельдорф", "düsseldorf", "лейпциг", "leipzig", "лейпціг",
                    "дортмунд", "dortmund", "єссен", "essen", "бремен", "bremen", "дрезден", "dresden", "нюрнберг",
                    "nürnberg", "дуйсбург", "duisburg", "бохум", "bochum", "вупперталь", "wuppertal", "билефельд",
                    "білефельд", "bielefeld", "бонн", "bonn", "мюнстер", "münster", "аугсбург", "augsburg", "висбаден",
                    "вісбаден", "wiesbaden", "гельзенкирхен", "гельзенкірхен", "gelsenkirchen", "мёнхенгладбах",
                    "mönchengladbach", "брауншвейг", "braunschweig");
            case "кёльн", "köln" -> of("кёльн", "köln");
            case "штутгарт", "stuttgart" -> of("штутгарт", "stuttgart");
            case "лейпциг", "leipzig" -> of("лейпциг", "leipzig");
            case "дортмунд", "dortmund" -> of("дортмунд", "dortmund");
            case "єссен", "essen" -> of("єссен", "essen");
            case "бремен", "bremen" -> of("бремен", "bremen");
            case "дрезден", "dresden" -> of("дрезден", "dresden");
            case "нюрнберг", "nürnberg", "nurnberg" -> of("нюрнберг", "nürnberg", "nurnberg");
            case "дуйсбург", "duisburg" -> of("дуйсбург", "duisburg");
            case "бохум", "bochum" -> of("бохум", "bochum");
            case "вупперталь", "wuppertal" -> of("вупперталь", "wuppertal");
            case "билефельд", "білефельд", "bielefeld" -> of("билефельд", "білефельд", "bielefeld");
            case "бонн", "bonn" -> of("бонн", "bonn");
            case "мюнстер", "münster", "munster" -> of("мюнстер", "münster", "munster");
            case "аугсбург", "augsburg" -> of("аугсбург", "augsburg");
            case "висбаден", "вісбаден", "wiesbaden" -> of("висбаден", "вісбаден", "wiesbaden");
            case "гельзенкирхен", "гельзенкірхен", "gelsenkirchen" -> of("гельзенкирхен", "гельзенкірхен", "gelsenkirchen");
            case "мёнхенгладбах", "мeнхенгладбах", "mönchengladbach", "monchengladbach" ->
                    of("мёнхенгладбах", "мeнхенгладбах", "mönchengladbach", "monchengladbach");
            case "брауншвейг", "braunschweig" -> of("брауншвейг", "braunschweig");
            case "дюссельдорф", "dusseldorf", "düsseldorf" -> of("дюссельдорф", "dusseldorf", "düsseldorf");
            case "berlin", "берлин", "берлін" -> of("berlin", "берлин", "берлін");
            case "мангейм", "mannheim" -> of("мангейм", "mannheim");
            case "ганновер", "hanover" -> of("ганновер", "hanover");
            case "гамбург", "hamburg" -> of("гамбург", "hamburg");
            case "мюнхен", "munich", "münchen" -> of("мюнхен", "munich", "münchen");
            case "karlsruhe", "карлсруе" -> of("karlsruhe", "карлсруе");
            case "франкфурт", "frankfurt" -> of("франкфурт", "frankfurt");

            case "israel", "израиль", "ізраїль" -> of("israel", "израиль", "ізраїль", "ישראל", "иерусалим", "jerusalem",
                    "ירושלים", "тель-авив", "тель авив", "tel aviv", "תל אביב", "хайфа", "haifa" , "חיפה", "ришон-ле-цион",
                    "ришон ле цион", "rishon lezion", "ראשון לציון", "петах-тиква", "петах тиква", "petah-tikva",
                    "פתח-תקווה", "ашдод", "ashdod", "אשדוד", "нетания", "netanya", "נתניה", "беэр-шева", "беэр шева",
                    "beersheba", "באר שבע", "бней-брак", "бней брак", "bnei marriage", "נישואי בני", "холон", "holon",
                    "рамат-ган", "рамат ган", "ramat-gan", "רמת-גן", "реховот", "rehovot", "רחובות", "ашкелон", "ashkelon",
                    "אשקלון", "бат-ям", "бат ям", "bat-yam", "בת-ים");
            case "иерусалим", "єрусалим", "jerusalem", "ירושלים" -> of("иерусалим", "jerusalem", "ירושלים");
            case "тель-авив", "тель авив", "тель-авів", "тель авів", "tel aviv", "תל אביב" -> of("тель-авив", "тель авив", "tel aviv", "תל אביב");
            case "хайфа", "haifa" , "חיפה" -> of("хайфа", "haifa", "חיפה");
            case "ришон ле цион", "ришон-ле-цион", "рішон ле ціон", "рішон-ле-ціон", "rishon lezion", "ראשון לציון" -> of("ришон-ле-цион", "ришон ле цион", "rishon lezion", "ראשון לציון");
            case "петах тиква", "петах-тиква", "петах тіква", "петах-тіква", "petah-tikva" , "פתח-תקווה" -> of("петах-тиква", "петах тиква", "petah-tikva", "פתח-תקווה");
            case "ашдод", "ashdod", "אשדוד" -> of("ашдод", "ashdod" , "אשדוד");
            case "нетания", "нетанія", "netanya", "נתניה" -> of("нетания", "netanya", "נתניה");
            case "беэр шева", "беэр-шева", "беер шева", "беер-шева", "beersheba", "באר שבע" -> of("беэр-шева", "beersheba", "באר שבע");
            case "бней брак", "бней-брак", "bnei marriage", "נישואי בני" -> of("бней-брак", "bnei marriage", "נישואי בני");
            case "холон", "holon" -> of("холон", "holon");
            case "рамат ган", "рамат-ган", "ramat-gan", "רמת-גן" -> of("рамат-ган", "ramat-gan", "רמת-גן");
            case "реховот", "rehovot", "רחובות" -> of("реховот", "rehovot", "רחובות");
            case "ашкелон", "ashkelon", "אשקלון" -> of("ашкелон", "ashkelon", "אשקלון");
            case "бат ям", "бат-ям", "bat-yam", "בת-ים" -> of("бат-ям", "bat-yam", "בת-ים");

            case "сша", "америка", "usa", "united states of america", "america" -> of("сша", "америка", "usa",
                    "united states of america", "america", "нью-йорк", "new york", "лос-анджелес", "лос анджелес",
                    "los angeles", "чикаго", "чікаго", "chicago", "хьюстон", "houston", "финикс", "фінікс", "phoenix",
                    "филадельфия", "філадельфія", "philadelphia", "сан-антонио", "сан антонио", "сан-антоніо",
                    "сан антоніо", "san sntonio", "сан-диего", "сан диего", "сан-діего", "сан діего", "san diego",
                    "даллас", "dallas", "сан-хосе", "san jose", "остин", "остін", "austin", "джэксонвилл", "джэксонвілл",
                    "jacksonville", "форт-уэрт", "форт уэрт", "форт-уерт", "форт уерт", "fort worth", "колумбус",
                    "columbus", "индианаполис", "індіанаполіс", "indianapolis", "шарлотт", "charlotte", "сан-франциско",
                    "сан франциско", "сан-франціско", "сан франціско", "san francisco","сиэтл", "сіэтл", "seattle",
                    "денвер", "denver","оклахома-сити", "оклахома сити", "оклахома-сіті", "оклахома сіті",
                    "oklahoma city", "нашвилл", "нашвілл", "nashville", "эль-пасо", "el paso", "вашингтон", "вашінгтон",
                    "washington", "бостон", "boston", "лас-вегас", "лас вегас", "las vegas", "портленд", "portland",
                    "детройт", "detroit", "луисвилл", "луісвілл", "louisville", "мемфис", "мемфіс", "memphis", "балтимор",
                    "балтімор", "baltimore", "сакраменто", "sacramento");
            case "нью-йорк", "new york" -> of("нью-йорк", "new york");
            case "лос-анджелес", "лос анджелес", "los angeles" -> of("лос-анджелес", "лос анджелес", "los angeles");
            case "чикаго", "чікаго", "chicago" -> of("чикаго", "чікаго", "chicago");
            case "хьюстон", "houston" -> of("хьюстон", "houston");
            case "финикс", "фінікс", "phoenix" -> of("финикс", "фінікс", "phoenix");
            case "филадельфия", "філадельфія", "philadelphia" -> of("филадельфия", "філадельфія", "philadelphia");
            case "сан-антонио", "сан антонио", "сан-антоніо", "сан антоніо", "san sntonio" ->
                    of("сан-антонио", "сан антонио", "сан-антоніо", "сан антоніо", "san sntonio");
            case "сан-диего", "сан диего", "сан-діего", "сан діего", "san diego" ->
                    of("сан-диего", "сан диего", "сан-діего", "сан діего", "san diego");
            case "даллас", "dallas" -> of("даллас", "dallas");
            case "сан-хосе", "san jose" -> of("сан-хосе", "san jose");
            case "остин", "остін", "austin" -> of("остин", "остін", "austin");
            case "джэксонвилл", "джэксонвілл", "jacksonville" -> of("джэксонвилл", "джэксонвілл", "jacksonville");
            case "форт-уэрт", "форт уэрт", "форт-уерт", "форт уерт", "fort worth" ->
                    of("форт-уэрт", "форт уэрт", "форт-уерт", "форт уерт", "fort worth");
            case "колумбус", "columbus" -> of("колумбус", "columbus");
            case "индианаполис", "індіанаполіс", "indianapolis" -> of("индианаполис", "індіанаполіс", "indianapolis");
            case "шарлотт", "charlotte" -> of("шарлотт", "charlotte");
            case "сан-франциско", "сан франциско", "сан-франціско", "сан франціско", "san francisco" ->
                    of("сан-франциско", "сан франциско", "сан-франціско", "сан франціско", "san francisco");
            case "сиэтл", "сіэтл", "seattle" -> of("сиэтл", "сіэтл", "seattle");
            case "денвер", "denver" -> of("денвер", "denver");
            case "оклахома-сити", "оклахома сити", "оклахома-сіті", "оклахома сіті", "oklahoma city" ->
                    of("оклахома-сити", "оклахома сити", "оклахома-сіті", "оклахома сіті", "oklahoma city");
            case "нашвилл", "нашвілл", "нeшвилл", "нeшвілл", "nashville" ->
                    of("нашвилл", "нашвілл", "nashville", "нeшвилл", "нeшвілл");
            case "эль-пасо", "el paso" -> of("эль-пасо", "el paso");
            case "вашингтон", "вашінгтон", "washington" -> of("вашингтон", "вашінгтон", "washington");
            case "бостон", "boston" -> of("бостон", "boston");
            case "лас-вегас", "лас вегас", "las vegas" -> of("лас-вегас", "лас вегас", "las vegas");
            case "портленд", "portland" -> of("портленд", "portland");
            case "детройт", "detroit" -> of("детройт", "detroit");
            case "луисвилл", "луісвілл", "louisville" -> of("луисвилл", "луісвілл", "louisville");
            case "мемфис", "мемфіс", "memphis" -> of("мемфис", "мемфіс", "memphis");
            case "балтимор", "балтімор", "baltimore" -> of("балтимор", "балтімор", "baltimore");
            case "сакраменто", "sacramento" -> of("сакраменто", "sacramento");

            case "france", "франция", "франція" -> of("france", "франция", "франція");
            case "minsk", "минск", "мінськ" -> of("minsk", "минск", "мінськ");
            default -> of(text);
        };
    }
}
