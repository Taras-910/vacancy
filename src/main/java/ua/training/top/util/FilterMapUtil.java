package ua.training.top.util;

import java.util.List;

import static java.util.List.of;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.HelpUtil.getCommonList;

public class FilterMapUtil {

    public List<String> getAria(String text) {
        return switch (text) {
            case "intern", "trainee", "интерн", "стажировка", "internship", "стажер" -> traineeAria;
            case "junior", "младший", "без опыта", "обучение" -> of("junior", "младший", "без опыта", "обучение");
            case "middle", "средний", "мідл" -> of("middle", "средний", "мідл");
            case "senior", "старший" -> of("senior", "старший");
            case "expert", "lead", "тимлид", "ведущий", "team lead" -> of("expert", "lead", "team lead", "ведущий", "тимлид");
            case "foreign", "за_рубежем", "за рубежем", "за кордоном", "другие страны" -> getCommonList(of(otherAria, foreignAria));
            case "remote", "relocate", "релокейт", "удаленно", "віддалено" -> remoteAria;
            case "ukraine", "україна", "украина", "ua" -> getCommonList(of(uaAria, citiesUA));
            case "київ", "киев", "kiev", "kyiv" -> of("київ", "киев", "kiev", "kyiv");
            case "харків", "харьков", "kharkiv" -> of("харків", "харьков", "kharkiv");
            case "дніпро", "днепр", "dnipro" -> of("дніпро", "днепр", "dnipro");
            case "одеса", "одесса", "odesa" -> of("одеса", "одесса", "odesa");
            case "львів", "львов", "lviv" -> of("львів", "львов", "lviv");
            case "ужгород", "uzhgorod" -> of("ужгород", "uzhgorod");
            case "полтава", "poltava" -> of("полтава", "poltava");
            case "луцк", "луцьк" -> of("луцк", "луцьк", "lutsk");
            case "житомир", "zhytomyr" -> of("житомир", "zhytomyr");
            case "хмельницький", "khmelnitsky" -> of("хмельницький", "khmelnitsky");
            case "вінниця", "винница", "vinnitsia" -> of("вінниця", "винница", "vinnitsia");
            case "рівне", "ровно", "rivne", "rovno" -> of("рівне", "ровно", "rivne", "rovno");
            case "миколаїв", "николаев", "mykolaiv" -> of("миколаїв", "николаев", "mykolaiv");
            case "чернігів", "чернигов", "chernigiv" -> of("чернігів", "чернигов", "chernigiv");
            case "тернопіль", "тернополь", "ternopil" -> of("тернопіль", "тернополь", "ternopil");
            case "чорновці", "черновцы", "chernivtsi" -> of("чорновці", "черновцы", "chernivtsi");
            case "запоріжжя", "запорожье", "zaporizhzhya" -> of("запоріжжя", "запорожье", "zaporizhzhya");
            case "івано-франківськ", "ивано-франковск", "ivano-frankivsk" -> of("івано-франківськ", "ивано-франковск", "ivano-frankivsk");

            case "польша", "польща", "poland", "polski", "pol" -> getCommonList(of(plAria, citiesPl));
            case "poznan", "познань" -> of("poznan", "познань");
            case "wroclaw", "вроцлав" -> of("wroclaw", "вроцлав");
            case "krakow", "краков", "краків" -> of("krakow", "краков", "краків");
            case "gdansk", "гданськ", "гданск" -> of("gdansk", "гданськ", "гданск");
            case "canada", "канада", "canad", "канад" -> getCommonList(of(caAria, citiesCa));
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
            case "британська колумбія", "британская колумбия", "british columbia" -> of("британська колумбія",
                    "британская колумбия", "british columbia");
            case "bulgaria", "болгария", "болгарія" -> getCommonList(of(bgAria, citiesBg));
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
            case "англія", "велика британія", "великобританія", "англия", "england", "united kingdom" -> getCommonList(of(ukAria, citiesUK));
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
            case "lee bank", "лі бенк", "ли бенк", "lee_bank", "лі_бенк", "ли_бенк" -> of("lee bank", "лі бенк",
                    "ли бенк", "lee_bank", "лі_бенк", "ли_бенк");
            case "germany", "германия", "німеччина" -> getCommonList(of(deAria, citiesDe));
            case "бонн", "bonn" -> of("бонн", "bonn");
            case "кёльн", "köln" -> of("кёльн", "köln");
            case "єссен", "essen" -> of("єссен", "essen");
            case "бохум", "bochum" -> of("бохум", "bochum");
            case "бремен", "bremen" -> of("бремен", "bremen");
            case "гамбург", "hamburg" -> of("гамбург", "hamburg");
            case "лейпциг", "leipzig" -> of("лейпциг", "leipzig");
            case "дрезден", "dresden" -> of("дрезден", "dresden");
            case "мангейм", "mannheim" -> of("мангейм", "mannheim");
            case "ганновер", "hanover" -> of("ганновер", "hanover");
            case "аугсбург", "augsburg" -> of("аугсбург", "augsburg");
            case "дортмунд", "dortmund" -> of("дортмунд", "dortmund");
            case "дуйсбург", "duisburg" -> of("дуйсбург", "duisburg");
            case "штутгарт", "stuttgart" -> of("штутгарт", "stuttgart");
            case "karlsruhe", "карлсруе" -> of("karlsruhe", "карлсруе");
            case "франкфурт", "frankfurt" -> of("франкфурт", "frankfurt");
            case "вупперталь", "wuppertal" -> of("вупперталь", "wuppertal");
            case "berlin", "берлин", "берлін" -> of("berlin", "берлин", "берлін");
            case "брауншвейг", "braunschweig" -> of("брауншвейг", "braunschweig");
            case "мюнхен", "munich", "münchen" -> of("мюнхен", "munich", "münchen");
            case "мюнстер", "münster", "munster" -> of("мюнстер", "münster", "munster");
            case "мёнхенгладбах", "мeнхенгладбах", "mönchengladbach", "monchengladbach" ->
                    of("мёнхенгладбах", "мeнхенгладбах", "mönchengladbach", "monchengladbach");
            case "нюрнберг", "nürnberg", "nurnberg" -> of("нюрнберг", "nürnberg", "nurnberg");
            case "висбаден", "вісбаден", "wiesbaden" -> of("висбаден", "вісбаден", "wiesbaden");
            case "билефельд", "білефельд", "bielefeld" -> of("билефельд", "білефельд", "bielefeld");
            case "дюссельдорф", "dusseldorf", "düsseldorf" -> of("дюссельдорф", "dusseldorf", "düsseldorf");
            case "гельзенкирхен", "гельзенкірхен", "gelsenkirchen" -> of("гельзенкирхен", "гельзенкірхен", "gelsenkirchen");
            case "чехия", "чехія", "сzech" -> getCommonList(of(czAria, citiesCz));
            case "брно", "brno" -> of("брно", "brno");
            case "прага", "prague" -> of("прага", "prague");
            case "плзень", "pilsen" -> of("плзень", "pilsen");
            case "острава", "ostrava" -> of("острава", "ostrava");
            case "оломоуц", "olomouc", "катовице" -> of("оломоуц", "olomouc", "катовице");
            case "карлові вари", "карлови вари", "karlovyvary" -> of("карлові вари", "карлови вари", "karlovyvary");
            case "словакия", "словакія", "словаччина", "slovakia" -> getCommonList(of(skAria, citiesSk));
            case "прешов", "prešov" -> of("прешов", "prešov");
            case "кошице", "кошіце", "košice" -> of("кошице", "кошіце", "košice");
            case "братислава", "братіслава", "bratislava" -> of("братислава", "братіслава", "bratislava");
            case "оаэ", "оае", "эмираты", "объединенные арабские эмираты", "арабские эмираты", "uae", "emirates",
                    "united arab emirates", "arab emirates" -> getCommonList(of(aeAria, citiesAe));
            case "шарджа",  "sharjah" -> of("шарджа",  "sharjah");
            case "дубай", "дубаи", "дубаі", "dubai" -> of("дубай", "дубаи", "дубаі", "dubai");
            case "abu dhabi", "абу-дабі", "абу-даби", "абу дабі", "абу даби" -> of("abu dhabi", "абу-дабі", "абу-даби",
                    "абу дабі", "абу даби");
            case "эль-айн", "ель-айн", "эль айн", "ель айн", "al ain" -> of("эль-айн", "ель-айн", "эль айн", "ель айн", "al ain");
            case "франция", "франція", "france" -> getCommonList(of(frAria, citiesFr));
            case "париж", "paris" -> of("париж", "paris");
            case "ренн", "rennes" -> of("ренн", "rennes");
            case "нант", "nantes" -> of("нант", "nantes");
            case "бордо", "bordeaux" -> of("бордо", "bordeaux");
            case "тулуза", "toulouse" -> of("тулуза", "toulouse");
            case "марсель", "marseille" -> of("марсель", "marseille");
            case "лион", "ліон", "lyon" -> of("лион", "ліон", "lyon");
            case "ницца", "ніцца", "nice" -> of("ницца", "ніцца", "nice");
            case "страсбург", "strasbourg" -> of("страсбург", "strasbourg");
            case "лилль", "лілль", "lille" -> of("лилль", "лілль", "lille");
            case "монпелье", "монпельє", "montpellier" -> of("монпелье", "монпельє", "montpellier");

            case "италия", "італія", "italy" -> getCommonList(of(itAria, citiesIt));
            case "рим", "roma" -> of("рим", "roma");
            case "лацио", "lazio" -> of("лацио", "lazio");
            case "генуя", "genova" -> of("генуя", "genova");
            case "турин", "torino" -> of("турин", "torino");
            case "неаполь", "napoli" -> of("неаполь", "napoli");
            case "палермо", "palermo" -> of("палермо", "palermo");
            case "болонья", "bologna" -> of("болонья", "bologna");
            case "милан", "мілан", "milano" -> of("милан", "мілан", "milano");
            case "пьемонт", "пємонт", "piemonte" -> of("пьемонт", "пємонт", "piemonte");
            case "сицилия", "сицілія", "sicilia" -> of("сицилия", "сицілія", "sicilia");
            case "лигурия", "лігурія", "liguria" -> of("лигурия", "лігурія", "liguria");
            case "кампания", "кампанія", "campania" -> of("кампания", "кампанія", "campania");
            case "флоренция", "флоренція", "firenze" -> of("флоренция", "флоренція", "firenze");
            case "ломбардия", "ломбардія", "lombardia" -> of("ломбардия", "ломбардія", "lombardia");
            case "эмилия-романья", "эмілія-романья", "emilia-romagna", "эмилия романья", "эмілія романья", "emilia romagna"
                    -> of("эмилия-романья", "эмілія-романья", "emilia-romagna", "эмилия романья", "эмілія романья", "emilia romagna");

            case "финляндия", "фінляндія", "finland" -> getCommonList(of(fiAria, citiesFi));
            case "хельсинки", "хельсінкі", "helsinki" -> of("хельсинки", "хельсінкі", "helsinki");
            case "еспоо", "эспоо", "espoo" -> of("еспоо", "эспоо", "espoo");
            case "тампере", "tampere" -> of("тампере", "tampere");
            case "вантаа", "vantaa" -> of("вантаа", "vantaa");
            case "турку", "turku" -> of("турку", "turku");

            case "швейцария", "швейцарія", "switzerland" -> getCommonList(of(chAria, citiesCh));
            case "берн", "bern" -> of("берн", "bern");
            case "базель", "basel" -> of("базель", "basel");
            case "женева", "genève" -> of("женева", "genève");
            case "люцерн", "luzern" -> of("люцерн", "luzern");
            case "лозанна", "lausanne" -> of("лозанна", "lausanne");
            case "винтертур", "winterthur" -> of("винтертур", "winterthur");
            case "цюрих", "цюріх", "zürich" -> of("цюрих", "цюріх", "zürich");
            case "санкт-галлен", "санкт галлен", "санкт-гален", "санкт гален", "st. gallen" -> of("санкт-галлен",
                    "санкт галлен", "санкт-гален", "санкт гален", "st. gallen");

            case "швеция", "швеція", "sweden" -> getCommonList(of(seAria, citiesSe));
            case "стокгольм", "stockholm" -> of("стокгольм", "stockholm");
            case "мальмё", "мальме", "malmö" -> of("мальмё", "мальме", "malmö");
            case "гётеборг", "гетеборг", "gothenburg" -> of("гётеборг", "гетеборг", "gothenburg");

            case "israel", "израиль", "ізраїль" -> getCommonList(of(ilAria, citiesIl));
            case "беэр шева", "беэр-шева", "беер шева", "беер-шева", "beersheba", "באר שבע" -> of("беэр-шева", "beersheba");
            case "тель-авив", "тель авив", "тель-авів", "тель авів", "tel aviv", "תל אביב" -> of("тель-авив", "тель авив",
                    "tel aviv", "תל אביב");
            case "бней брак", "бней-брак", "bnei marriage", "נישואי בני" -> of("бней-брак", "bnei marriage", "נישואי בני");
            case "ришон ле цион", "ришон-ле-цион", "рішон ле ціон", "рішон-ле-ціон", "rishon lezion", "ראשון לציון"
                    -> of("ришон-ле-цион", "ришон ле цион", "rishon lezion", "ראשון לציון");
            case "иерусалим", "єрусалим", "jerusalem", "ירושלים" -> of("иерусалим", "jerusalem", "ירושלים");
            case "рамат ган", "рамат-ган", "ramat-gan", "רמת-גן" -> of("рамат-ган", "ramat-gan", "רמת-גן");
            case "петах тиква", "петах-тиква", "петах тіква", "петах-тіква", "petah-tikva" , "פתח-תקווה"
                    -> of("петах-тиква", "петах тиква", "petah-tikva", "פתח-תקווה");
            case "нетания", "нетанія", "netanya", "נתניה" -> of("нетания", "netanya", "נתניה");
            case "бат ям", "бат-ям", "bat-yam", "בת-ים" -> of("бат-ям", "bat-yam", "בת-ים");
            case "ашкелон", "ashkelon", "אשקלון" -> of("ашкелон", "ashkelon", "אשקלון");
            case "реховот", "rehovot", "רחובות" -> of("реховот", "rehovot", "רחובות");
            case "ашдод", "ashdod", "אשדוד" -> of("ашдод", "ashdod" , "אשדוד");
            case "хайфа", "haifa" , "חיפה" -> of("хайфа", "haifa", "חיפה");
            case "холон", "holon" -> of("холон", "holon");

            case "сша", "америка", "usa", "united states of america", "america" -> getCommonList(of(usAria, citiesUS));
            case "индианаполис", "індіанаполіс", "indianapolis" -> of("индианаполис", "індіанаполіс", "indianapolis");
            case "лос-анджелес", "лос анджелес", "los angeles" -> of("лос-анджелес", "лос анджелес", "los angeles");
            case "филадельфия", "філадельфія", "philadelphia" -> of("филадельфия", "філадельфія", "philadelphia");
            case "джэксонвилл", "джэксонвілл", "jacksonville" -> of("джэксонвилл", "джэксонвілл", "jacksonville");
            case "вашингтон", "вашінгтон", "washington" -> of("вашингтон", "вашінгтон", "washington");
            case "лас-вегас", "лас вегас", "las vegas" -> of("лас-вегас", "лас вегас", "las vegas");
            case "луисвилл", "луісвілл", "louisville" -> of("луисвилл", "луісвілл", "louisville");
            case "балтимор", "балтімор", "baltimore" -> of("балтимор", "балтімор", "baltimore");
            case "нью-йорк", "нью йорк", "new york" -> of("нью-йорк", "нью йорк", "new york");
            case "чикаго", "чікаго", "chicago" -> of("чикаго", "чікаго", "chicago");
            case "финикс", "фінікс", "phoenix" -> of("финикс", "фінікс", "phoenix");
            case "мемфис", "мемфіс", "memphis" -> of("мемфис", "мемфіс", "memphis");
            case "сиэтл", "сіэтл", "seattle" -> of("сиэтл", "сіэтл", "seattle");
            case "остин", "остін", "austin" -> of("остин", "остін", "austin");
            case "сакраменто", "sacramento" -> of("сакраменто", "sacramento");
            case "сан-хосе", "san jose" -> of("сан-хосе", "san jose");
            case "колумбус", "columbus" -> of("колумбус", "columbus");
            case "шарлотт", "charlotte" -> of("шарлотт", "charlotte");
            case "портленд", "portland" -> of("портленд", "portland");
            case "эль-пасо", "el paso" -> of("эль-пасо", "el paso");
            case "хьюстон", "houston" -> of("хьюстон", "houston");
            case "детройт", "detroit" -> of("детройт", "detroit");
            case "даллас", "dallas" -> of("даллас", "dallas");
            case "денвер", "denver" -> of("денвер", "denver");
            case "бостон", "boston" -> of("бостон", "boston");
            case "сан-франциско", "сан франциско", "сан-франціско", "сан франціско", "san francisco" ->
                    of("сан-франциско", "сан франциско", "сан-франціско", "сан франціско", "san francisco");
            case "оклахома-сити", "оклахома сити", "оклахома-сіті", "оклахома сіті", "oklahoma city" ->
                    of("оклахома-сити", "оклахома сити", "оклахома-сіті", "оклахома сіті", "oklahoma city");
            case "сан-антонио", "сан антонио", "сан-антоніо", "сан антоніо", "san sntonio" ->
                    of("сан-антонио", "сан антонио", "сан-антоніо", "сан антоніо", "san sntonio");
            case "сан-диего", "сан диего", "сан-діего", "сан діего", "san diego" ->
                    of("сан-диего", "сан диего", "сан-діего", "сан діего", "san diego");
            case "форт-уэрт", "форт уэрт", "форт-уерт", "форт уерт", "fort worth" ->
                    of("форт-уэрт", "форт уэрт", "форт-уерт", "форт уерт", "fort worth");
            case "нашвилл", "нашвілл", "нeшвилл", "нeшвілл", "nashville" ->
                    of("нашвилл", "нашвілл", "nashville", "нeшвилл", "нeшвілл");
            case "minsk", "минск", "мінськ" -> of("minsk", "минск", "мінськ");
            default -> of(text);
        };
    }
}
