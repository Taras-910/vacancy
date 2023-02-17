package ua.training.top.util.aggregatorUtil.data;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.regex.Pattern;

import static java.util.List.of;
import static ua.training.top.util.aggregatorUtil.data.CommonUtil.*;
import static ua.training.top.util.aggregatorUtil.data.ConstantsUtil.*;

public class WorkplaceUtil {

    public static String getDjinni(String workplace) {
        String region = isMatches(ImmutableList.of(uaAria, citiesUA), workplace) ? "UKR" :
                        isMatches(ImmutableList.of(plAria, citiesPl), workplace) ? "POL" :
                        isMatches(ImmutableList.of(deAria, citiesDe), workplace) ? "DEU" :
                        isMatches(List.of(caAria, citiesCa, usAria, citiesUS), workplace) ? "other" : "eu";
        return getJoin("region=", region, !isMatch(citiesUA, workplace) ? "" : "&location=" + getUA_en(workplace).toLowerCase());
    }

    public static String getITJobs(String workplace) {
        return switch (workplace) {
            case "all", "canada", "канада", "другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном" -> "all";
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "remote";
            case "ontario", "онтарио", "онтаріо" -> "&location=Ontario&location-id=ON&location-type=2";
            case "альберта", "alberta" -> "&location=Alberta&location-id=AB&location-type=2";
            case "ottawa", "оттава" -> "&location=Ottawa%2C+ON&location-id=315580&location-type=1";
            case "quebec", "квебек" -> "&location=Quebec%2C+QC&location-id=117253&location-type=1";
            case "toronto", "торонто" -> "&location=Toronto%2C+ON&location-id=481104&location-type=1";
            case "montréal", "монреаль" -> "&location=Montreal%2C+QC&location-id=182156&location-type=1";
            case "манитоба", "манітоба", "manitoba" -> "&location=Manitoba&location-id=MB&location-type=2";
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

    public static String getJobsDouForeign(String city){
        return  switch (city) {
            case "варшава", "warsaw" -> "Варшава";
            case "вроцлав", "wroclaw" -> "Вроцлав";
            case "гданськ", "гданск", "gdansk" -> "Гданськ";
            case "краків", "краков", "krakow" -> "Краків";
            case "познань", "poznan" -> "Познань";
            case "лодзь", "lodz" -> "Лодзь";
            case "варна", "varna" -> "Варна";
            case "прага", "prague" -> "Прага";
            case "лісабон", "лиссабон", "lisbon" -> "Лісабон";
            case "лімасол", "лимасол", "limasol" -> "Лімасол";
            case "тбілісі", "тбилиси", "tbilisi" -> "Тбілісі";
            case "таллінн", "таллинн", "tallinn" -> "Таллінн";
            case "вільнюс", "вильнюс", "vilnius" -> "Вільнюс";
            case "берлін", "берлин", "berlin" -> "Берлін";
            case "софія", "софия", "sofia" -> "Софія";
            case "зелена гура" -> "Зелена%20Гура";
            case "рига", "riga" -> "Рига";
            default -> "Польща"; // Украина, all
        };
    }

    public static String getLinkedin(String city){
        return switch (city) {
            case "ужгород" -> "&location=Uzhhorod%2C%20Zakarpattya%2C%20Ukraine&geoId=106974374";
            case "львів", "львов", "lviv" -> "&&location=Lviv%2C%20Lviv%2C%20Ukraine&geoId=104983263";
            case "харьків", "харьков", "kharkiv" -> "&location=Kharkiv%2C%20Kharkiv%2C%20Ukraine&geoId=103352426";
            case "київ", "киев", "kyiv", "kiev" -> "&location=Kyiv%2C%20Kyiv%20City%2C%20Ukraine&geoId=104035893";
            case "одеса", "одесса", "odessa", "odesa" -> "&location=Odessa%2C%20Odessa%2C%20Ukraine&geoId=100182723";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "&location=Dnipro%2C%20Dnipropetrovsk%2C%20Ukraine&geoId=103663309";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "&location=Zaporizhzhya%2C%20Zaporizhzhya%2C%20Ukraine&geoId=104184784";
            case "черкаси", "черкассы", "cherkasy", "cherkassy" -> "&location=Cherkasy%2C%20Cherkasy%2C%20Ukraine&geoId=104320082";
            case "вінниця", "винница", "vinnitsya" -> "&location=Vinnytsya%2C%20Vinnytsya%2C%20Ukraine&geoId=106030501";

            case "канада", "canada" -> "&location=20Canada&geoId=101174742";
            case "toronto", "торонто", "ontario", "онтарио", "онтаріо" -> "&location=Toronto%2C%20Ontario%2C%20Canada&geoId=100025096";
            case "mississauga", "миссиссога", "міссісога" -> "&location=Mississauga%2C%20Ontario%2C%20Canada&geoId=101788145";
            case "edmonton", "едмонтон", "эдмонтон" -> "&location=Edmonton%2C%20Alberta%2C%20Canada&geoId=106535873";
            case "brampton", "брэмптон", "бремптон" -> "&location=Brampton%2C%20Ontario%2C%20Canada&geoId=104669182";
            case "calgary", "калгарі", "калгари" -> "&location=Calgary%2C%20Alberta%2C%20Canada&geoId=102199904";
            case "vancouver", "ванкувер", "британська колумбія", "британская колумбия", "british columbia"
                    -> "&location=Vancouver%2C%20British%20Columbia%2C%20Canada&geoId=103366113";
            case "ottawa", "оттава" -> "&location=Ottawa%2C%20Ontario%2C%20Canada&geoId=106234700";
            case "виннипег", "вінніпег", "winnipeg", "манитоба", "манітоба", "manitoba"
                    -> "&location=Winnipeg%2C%20Manitoba%2C%20Canada&geoId=101213860";

            case "польща", "польша", "poland", "pol" -> "&location=Poland&geoId=105072130";
            case "lodz", "лодзь" -> "&location=Łódź%2C%20Łódzkie%2C%20Poland&geoId=106518625";
            case "wroclaw", "вроцлав" -> "&location=Wroclaw%20Metropolitan%20Area&geoId=105001681";
            case "варшава", "warszawa" -> "&location=Warsaw%2C%20Mazowieckie%2C%20Poland&geoId=105076658";
            case "poznan", "познань" -> "&location=Poznań%2C%20Wielkopolskie%2C%20Poland&geoId=100231202";
            case "gdansk", "гданськ", "гданск" -> "&location=Gdańsk%2C%20Pomorskie%2C%20Poland&geoId=104070045";
            case "krakow", "краков", "краків" -> "&location=Krakowski%2C%20Małopolskie%2C%20Poland&geoId=103263110";
            case "katowice", "катовіце", "катовице" -> "&location=Katowice%2C%20Śląskie%2C%20Poland&geoId=104550910";

            case "англія", "англия", "великобританія", "великобритания", "england", "united kingdom" -> "&location=United%20Kingdom&geoId=101165590";
            case "cambridgeshire", "кембріджшир", "кембриджшир" -> "&location=Cambridgeshire%2C%20England%2C%20United%20Kingdom&geoId=102057244";
            case "westminster", "вестмінстер", "вестминстер" -> "&location=Westminster%2C%20England%2C%20United%20Kingdom&geoId=106628425";
            case "birmingham","бірмінгем", "бирмингем" -> "&location=Birmingham%2C%20England%2C%20United%20Kingdom&geoId=100356971";
            case "lanarkshire", "ланаркшир" -> "&location=North%20Lanarkshire%2C%20Scotland%2C%20United%20Kingdom&geoId=103444309";
            case "shoreditch", "шордітч", "шордитч" -> "&location=Shoreditch%2C%20England%2C%20United%20Kingdom&geoId=102848790";
            case "scotland", "шотландія", "шотландия" -> "&location=Glasgow%2C%20Scotland%2C%20United%20Kingdom&geoId=102681496";
            case "milton", "мілтон", "милтон" -> "&location=Milton%20Keynes%2C%20England%2C%20United%20Kingdom&geoId=101356765";
            case "yorkshire", "йоркшир" -> "&location=North%20Lanarkshire%2C%20Scotland%2C%20United%20Kingdom&geoId=104194865";
            case "bristol", "брістоль", "бристоль" -> "&location=Bristol%2C%20England%2C%20United%20Kingdom&geoId=105982022";
            case "clerkenwell", "клеркенвелл" -> "&location=Clerkenwell%2C%20England%2C%20United%20Kingdom&geoId=107771707";
            case "manchester", "манчестер" -> "&location=Manchester%2C%20England%2C%20United%20Kingdom&geoId=114456426";
            case "southwark", "саутворк" -> "&location=Southwark%2C%20England%2C%20United%20Kingdom&geoId=109793197";
            case "hampshire", "гемпшир" -> "&location=Hampshire%2C%20England%2C%20United%20Kingdom&geoId=106123994";
            case "berkshire", "беркшир" -> "&location=Windsor%2C%20England%2C%20United%20Kingdom&geoId=106443489";
            case "moorgate", "мургейт" -> "&location=Moorgate%2C%20England%2C%20United%20Kingdom&geoId=110555673";
            case "leeds", "лідс","лидс" -> "&location=Leeds%2C%20England%2C%20United%20Kingdom&geoId=102943586";
            case "glasgow","глазго" -> "&location=Glasgow%2C%20Scotland%2C%20United%20Kingdom&geoId=102681496";
            case "london", "лондон" -> "&location=London%2C%20England%2C%20United%20Kingdom&geoId=102257491";
            case "soho", "сохо" -> "&location=Soho%2C%20England%2C%20United%20Kingdom&geoId=103381863";
            case "nottinghamshire", "ноттінгемшир", "ноттингемшир"
                    -> "&location=Nottinghamshire%2C%20England%2C%20United%20Kingdom&geoId=100245663";

            case "българия", "болгария", "болгарія", "bulgaria" -> "&location=Bulgaria&geoId=105333783";
            case "варна", "varna" -> "&location=Varna%2C%20Varna%2C%20Bulgaria&geoId=90010197";
            case "ловеч", "lovech" -> "&location=Lovech%2C+Lovech%2C+Bulgaria&geoId=114680252";
            case "шумен", "shumen" -> "&location=Shumen%2C+Shumen%2C+Bulgaria&geoId=110401202";
            case "плевен", "pleven" -> "&location=Pleven%2C+Pleven%2C+Bulgaria&geoId=113129693";
            case "бургас", "burgas" -> "&location=Burgas%2C%20Burgas%2C%20Bulgaria&geoId=90010196";
            case "смолян", "smolyan" -> "&location=Smolyan%2C+Smolyan%2C+Bulgaria&geoId=105837698";
            case "банско", "bansko" -> "&location=Bansko%2C+Blagoevgrad%2C+Bulgaria&geoId=114686435";
            case "русе", "rousse", "ruse" -> "&location=Ruse%2C%20Ruse%2C%20Bulgaria&geoId=111411131";
            case "добрич", "dobrich" -> "&location=Dobrich%2C%20Dobrich%2C%20Bulgaria&geoId=103914939";
            case "софия", "софія", "sofia" -> "&location=Sofia%2C%20Sofia%20City%2C%20Bulgaria&geoId=103835801";
            case "пловдив", "пловдів", "plovdiv" -> "&location=Plovdiv%2C%20Plovdiv%2C%20Bulgaria&geoId=100783188";
            case "благоевград", "blagoevgrad" -> "&location=Blagoevgrad%2C+Blagoevgrad%2C+Bulgaria&geoId=109017058";
            case "силистра", "сілістра", "silistra" -> "&location=Silistra%2C%20Silistra%2C%20Bulgaria&geoId=105276859";

            case "німеччина", "германия", "germany" -> "&location=Germany&geoId=101282230";
            case "мюнхен", "munich" -> "&location=Munich%2C%20Bavaria%2C%20Germany&geoId=100477049";
            case "берлин", "berlin", "берлін" -> "&location=Berlin%2C%20Berlin%2C%20Germany&geoId=101283667";
            case "ганновер", "hanover" -> "&location=Hannover%2C%20Lower%20Saxony%2C%20Germany&geoId=102177763";
            case "маннхейм", "mannheim" -> "&location=Mannheim%2C%20Baden-Württemberg%2C%20Germany&geoId=104877661";
            case "karlsruhe", "карлсруе" -> "&location=Karlsruhe%2C%20Baden-Württemberg%2C%20Germany&geoId=106523486";
            case "франкфурт на майне", "frankfurt" -> "&location=Frankfurt%20am%20Main%2C%20Hesse%2C%20Germany&geoId=106150090";
            case "дюссельдорф", "dusseldorf" -> "&location=Düsseldorf%2C%20North%20Rhine-Westphalia%2C%20Germany&geoId=104008204";

            case "ізраїль", "ізраіль", "израиль", "israel", "ישראל" -> "&location=Israel&geoId=101620260";
            case "рамат-ган", "рамат ган", "ramat-gan" , "רמת-גן" -> "&location=Рамат%20Ган%2C%20Тель%20Авив%2C%20Израиль&geoId=102981967";
            case "нетания", "нетанія", "netanya" , "נתניה" -> "&location=Нетания%2C%20Центральный%20округ%2C%20Израиль&geoId=102178024";
            case "ришон-ле-цион", "ришон ле цион", "рішон-ле-ціон", "рішон ле ціон", "рішон-ле-цион", "рішон ле цион",
                    "rishon lezion" , "ראשון לציון" -> "&location=Ришон%20Ле-Цион%2C%20Центральный%20округ%2C%20Израиль&geoId=101028188";
            case "бат-ям", "бат ям", "bat-yam" , "בת-ים" -> "&location=Бат%20Ям%2C%20Тель%20Авив%2C%20Израиль&geoId=100181358";
            case "реховот", "rehovot" , "רחובות" -> "&location=Реховот%2C%20Центральный%20округ%2C%20Израиль&geoId=104300930";
            case "ашкелон", "ashkelon" , "אשקלון" -> "&location=Ашкелон%2C%20Южный%20округ%2C%20Израиль&geoId=102870352";
            case "тель-авив", "тель авив", "тель-авів", "тель авів", "тел-авив", "тел авив", "тел-авів", "тел авів",
                    "tel aviv", "תל אביב" -> "&location=Тель%20Авив-Яфо%2C%20Тель%20Авив%2C%20Израиль&geoId=101570771";
            case "ашдод", "ashdod" , "אשדוד" -> "&location=Ашдод%2C%20Южный%20округ%2C%20Израиль&geoId=101287413";
            case "хайфа", "haifa" , "חיפה" -> "&location=Хайфа%2C%20Хайфа%2C%20Израиль&geoId=101939752";
            case "петах-тиква", "петах тиква", "петах-тіква", "петах тіква", "petah-tikva" , "פתח-תקווה"
                    -> "&location=Петах%20Тиква%2C%20Центральный%20округ%2C%20Израиль&geoId=105752964";
            case "холон", "holon" -> "&location=Холон%2C%20Тель%20Авив%2C%20Израиль&geoId=104624617";
            case "иерусалим", "єрусалим", "jerusalem", "ירושלים"
                    -> "&location=Иерусалим%2C%20Центральная%20Ява%2C%20Индонезия&geoId=104618461";
            case "беэр-шева", "беэр шева", "беер-шева", "беер шева", "beersheba" , "באר שבע"
                    -> "&location=Беэр%20Шева%2C%20Южный%20округ%2C%20Израиль&geoId=106471739";
            case "бней-брак", "бней брак", "bnei marriage" , "נישואי בני"
                    -> "&location=Бней-Брак%2C%20Тель%20Авив%2C%20Израиль&geoId=103182455";

            case "оаэ", "оае", "эмираты", "emirates" -> "&location=United%20Arab%20Emirates&geoId=104305776";
            case "netherlands", "голландия", "нідерланди" -> "&location=Netherlands&geoId=102890719";
            case "швейцарія", "швейцария", "switzerland" -> "&location=Switzerland&geoId=106693272";
            case "філіппіни", "филиппины", "philippines" -> "&location=Philippines&geoId=103121230";
            case "австралія", "австралия", "australia" -> "&location=Australia&geoId=101452733";
            case "сінгапур", "сингапур", "singapore" -> "&location=Singapore&geoId=102454443";
            case "фінляндія", "финляндия", "finland" -> "&location=Finland&geoId=100456013";
            case "словакия", "словакія", "slovakia" -> "&location=Slovakia&geoId=103119917";
            case "таиланд", "таіланд", "thailand" -> "location=Thailand&geoId=105146118";
            case "норвегія", "норвегия", "norway" -> "&location=Norway&geoId=103819153";
            case "бельгия",  "бельгія", "belgium" -> "&location=Belgium&geoId=100565514";
            case "естонія", "эстония", "estonia" -> "&location=Estonia&geoId=102974008";
            case "венгрия", "венргія", "hungary" -> "&location=Hungary&geoId=100288700";
            case "румыния", "руминія", "romania" -> "&location=Romania&geoId=106670623";
            case "франція", "франция", "france" -> "&location=France&geoId=105015875";
            case "грузия", "грузія", "georgia" -> "&location=Georgia&geoId=106315325";
            case "черногория", "montenegro" -> "&location=Montenegro&geoId=100733275";
            case "дания",  "данія", "denmark" -> "&location=Denmark&geoId=104514075";
            case "чехія", "чехия", "czechia" -> "&location=Czechia&geoId=104508036";
            case "турция", "турція", "turkey" -> "&location=Turkey&geoId=102105699";
            case "греция", "греція", "greece" -> "&location=Greece&geoId=104677530";
            case "латвия", "латвія", "latvia" -> "&location=Latvia&geoId=104341318";
            case "швеція", "швеция", "sweden" -> "&location=Sweden&geoId=105117694";
            case "італія", "италия", "italy" -> "&location=Italy&geoId=103350119";
            case "кипр", "кіпр", "cyprus" -> "&location=Cyprus&geoId=106774002";
            case "литва", "lithuania" -> "&location=Lithuania&geoId=101464403";
            case "мінськ", "минск" -> "&location=Minsk%2C%20Minsk%2C%20Belarus&geoId=105415465";
            case "сша", "соединенные штаты америки", "соединенные штаты", "united states of america", "usa" ->
                    "&location=United%20States&geoId=103644278";
            default -> "&location=Ukraine&geoId=102264497"; //"украина", "україна", "ukraine", "all"
        };
    }

    public static String getRabota(String workplace) {
        return switch (workplace) {
            case "remote", "relocate", "удаленно", "віддалено", "all", "украина", "україна", "ukraine" -> "украина";
            case "київ", "киев", "kyiv", "kiev" -> "киев";
            case "одеса", "одесса", "odessa" -> "одесса";
            case "львів", "львов", "lviv" -> "львов";
            case "вінниця", "винница" -> "винница";
            case "харьків", "харьков", "kharkiv" -> "харьков";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "днепр";
            case "миколаїв", "николаев", "mykolaiv" -> "николаев";
            case "чернігів", "чернигов", "chernigiv" -> "чернигов";
            case "чорновці", "черновцы", "chernivtsi" -> "черновцы";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "запорожье";
            default -> !isMatch(citiesUA, workplace) || isMatch(foreignAria, workplace) ? "другие_страны" : "украина";
        };
    }

    public static String getIndeed(String workplace) {
        return switch (workplace) {
            case "украина", "україна", "ukraine", "all" -> "&l=Украина";
            case "remote", "relocate", "удаленно", "віддалено", "віддалена робота" -> "&rbl=Удаленно&jlid=f00b7fa4b055cc00";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "&rbl=Днепр,+Днепропетровская+область&jlid=030c410a355d8014";
            case "харьків", "харьков", "kharkiv" -> "&rbl=Харьков&jlid=6fb70c8a2ab37b1f";
            case "київ", "киев", "kyiv", "kiev" -> "&rbl=Киев&jlid=e9ab1a23f8e591f1";
            case "одеса", "одесса", "odessa" -> "&rbl=Одесса&jlid=240fe96bd3c6e402";
            case "львів", "львов", "lviv" -> "&rbl=Львов&jlid=6ea57808cf02b292";
            default -> "-1";
        };
    }

    public static String getCa(String workplace) {
        return switch (workplace) {
            case "canada", "all", "канада" -> "Canada";
            case "альберта", "alberta" -> "Alberta";
            case "ottawa", "оттава" -> "Ottawa%2C+ON";
            case "toronto", "торонто" -> "Toronto%2C+ON";
            case "ontario", "онтарио", "онтаріо" -> "Ontario";
            case "vancouver", "ванкувер" -> "Vancouver%2C+BC";
            case "манитоба", "манітоба", "manitoba" -> "Manitoba";
            case "calgary", "калгарі", "калгари" -> "Calgary%2C+AB";
            case "brampton", "брэмптон", "бремптон" -> "Brampton%2C+ON";
            case "edmonton", "едмонтон", "эдмонтон" -> "Edmonton%2C+AB";
            case "mississauga", "миссиссога", "міссісога" -> "Mississauga%2C+ON";
            case "британська колумбія", "британская колумбия", "british columbia" -> "British+Columbia";
            case "remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота" -> "Remote";
            default -> "";
        };
    }

    public static String getUS(String workplace) {
        return switch (workplace) {
            case "бостон", "boston" -> "Boston%2C%20MA";
            case "даллас", "dallas" -> "Dallas%2C%20TX";
            case "денвер", "denver" -> "Denver%2C%20CO";
            case "хьюстон", "houston" -> "Houston%2C%20TX";
            case "детройт", "detroit" -> "Detroit%2C%20MI";
            case "портленд", "portland" -> "Portland%2C%20OR";
            case "эль-пасо", "el paso" -> "El%20Paso%2C%20TX";
            case "колумбус", "columbus" -> "Columbus%2C%20OH";
            case "шарлотт", "charlotte" -> "Charlotte%2C%20NC";
            case "сан-хосе", "san jose" -> "San%20Jose%2C%20CA";
            case "остин", "остін", "austin" -> "Austin%2C%20TX";
            case "нью-йорк", "new york" -> "New%20York%2C%20NY";
            case "сиэтл", "сіэтл", "seattle" -> "Seattle%2C%20WA";
            case "чикаго", "чікаго", "chicago" -> "Chicago%2C%20IL";
            case "финикс", "фінікс", "phoenix" -> "Phoenix%2C%20AZ";
            case "мемфис", "мемфіс", "memphis" -> "Memphis%2C%20TX";
            case "сакраменто", "sacramento" -> "Sacramento%2C%20CA";
            case "нашвилл", "нашвілл", "nashville" -> "Nashville%2C%20TN";
            case "балтимор", "балтімор", "baltimore" -> "Baltimore%2C%20MD";
            case "луисвилл", "луісвілл", "louisville" -> "Louisville%2C%20KY";
            case "лас-вегас", "лас вегас", "las vegas" -> "Las%20Vegas%2C%20NV";
            case "вашингтон", "вашінгтон", "washington" -> "Washington%2C%20PA";
            case "филадельфия", "філадельфія", "philadelphia" -> "Philadelphia%2C%20PA";
            case "индианаполис", "індіанаполіс", "indianapolis" -> "Indianapolis%2C%20IN";
            case "лос-анджелес", "лос анджелес", "los angeles" -> "Los%20Angeles%2C%20CA";
            case "сан-диего", "сан диего", "сан-діего", "сан діего", "san diego" -> "San%20Diego%2C%20CA";
            case "форт-уэрт", "форт уэрт", "форт-уерт", "форт уерт", "fort worth" -> "Jacksonville%2C%20FL";
            case "сан-антонио", "сан антонио", "сан-антоніо", "сан антоніо", "san antonio" -> "San%20Antonio%2C%20TX";
            case "сан-франциско", "сан франциско", "сан-франціско", "сан франціско", "san francisco" -> "San%20Francisco%2C%20CA";
            case "оклахома-сити", "оклахома сити", "оклахома-сіті", "оклахома сіті", "oklahoma city" -> "Oklahoma%20City%2C%20OK";
            default -> ""; // "сша", "америка", "usa", "united states of america", "america"
        };
    }

    public static String getJoobleUA(String workplace){
        return isMatch(citiesUA, workplace) ? getUA_ua(workplace) : switch (workplace) {
            case "канада", "canada" -> "Канада";
            case "варшава" -> "Варшава%2C%20Польща";
            case "польша", "польща", "poland" -> "Польща";
            case "краків", "краков" -> "Краків%2C%20Польща";
            case "мінськ", "минск" -> "Мінськ%2C%20Білорусь";
            case "германия", "німеччина", "germany" -> "Німеччина";
            case "другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном" -> "за%20кордоном";
            default -> workplace;
        };
    }

    public static String getUA_ua(String city){
        return  switch (city) {
            case "київ", "киев", "kyiv", "kiev" -> "Київ";
            case "луцк", "луцьк" -> "Луцьк";
            case "суми", "сумы", "sumi" -> "Суми";
            case "полтава", "poltava" -> "Полтава";
            case "житомир", "zhytomyr" -> "Житомир";
            case "ужгород", "uzhgorod" -> "Ужгород";
            case "львів", "львов", "lviv" -> "Львів";
            case "харків", "харьков", "kharkiv" -> "Харків";
            case "рівне", "ровно", "rivne", "rovno" -> "Рівне";
            case "вінниця", "винница", "vinnitsia" -> "Вінниця";
            case "одеса", "одесса", "odessa", "odesa" -> "Одеса";
            case "хмельницький", "khmelnitsky" -> "Хмельницький";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "Дніпро";
            case "миколаїв", "николаев", "mykolaiv" -> "Миколаїв";
            case "чернігів", "чернигов", "chernigiv" -> "Чернігів";
            case "чернівці", "черновцы", "chernivtsi" -> "Чернівці";
            case "тернопіль", "тернополь", "ternopil" -> "Тернопіль";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "Запоріжжя";
            case "черкаси", "черкассы", "cherkasy", "cherkassy" -> "Черкаси";
            case "івано-франківськ", "ивано-франковск", "ivano-frankivsk" -> "Івано-Франківськ";
            default -> "";
        };
    }

    public static String getUA_en(String workplace) {
        return switch (workplace) {
            case "київ", "киев", "kiev", "kyiv" -> "Kyiv";
            case "чорновці", "черновцы", "chernivtsi" -> "Chernivtsi";
            case "чернігів", "чернигов", "chernigiv" -> "Chernigiv";
            case "вінниця", "винница", "vinnitsia" -> "Vinnitsia";
            case "харків", "харьков", "kharkiv" -> "Kharkiv";
            case "одеса", "одесса", "odessa" -> "Odesa";
            case "львів", "львов", "lviv" -> "Lviv";
            case "ужгород", "uzhgorod" -> "Uzhgorod";
            case "дніпро", "днепр", "dnipro", "dnepr" -> "Dnipro";
            case "миколаїв", "николаев", "mykolaiv" -> "Mykolaiv";
            case "тернопіль", "тернополь", "ternopil" -> "Ternopil";
            case "івано-франківськ", "ивано-франковск" -> "Ivano-Frankivsk";
            case "запоріжжя", "запорожье", "zaporizhzhya" -> "Zaporizhzhya";
            default -> "";
        };
    }

    public static String getPl(String city) {
        return switch (city) {
            case "варшава", "warszawa" -> "Warszawa";
            case "krakow", "краков", "краків" -> "Krakow";
            case "gdansk", "гданськ", "гданск" -> "Gdansk";
            case "katowice", "катовіце", "катовице" -> "Katowice";
            case "wroclaw", "вроцлав" -> "Wroclaw";
            case "poznan", "познань" -> "Poznan";
            case "lodz", "лодзь" -> "Lodz";
            default -> "";
        };
    }

    public static String getCZ(String city) {
        return switch (city) {
            case "брно", "brno" -> "Brno";
            case "прага", "prague" -> "Prague";
            case "плзень", "pilsen" -> "Pilsen";
            case "острава", "ostrava" -> "Ostrava";
            case "оломоуц", "olomouc", "катовице" -> "Katowice";
            case "карлові вари", "карлови вари", "karlovyvary" -> "Karlovy Vary";
            default -> "";
        };
    }

    public static String getSK(String city) {
        return switch (city) {
            case "братислава", "братіслава", "bratislava" -> "Bratislava";
            case "кошице", "кошіце", "košice" -> "Košice";
            case "прешов", "prešov" -> "Ostrava";
            default -> "";
        };
    }

    public static String getBG_en(String workplace) {
        return  switch (workplace) {
            case "софия", "софія", "sofia" -> "sofia/";
            case "варна", "varna" -> "varna/";
            case "русе", "rousse" -> "rousse/";
            case "ловеч", "lovech" -> "lovech/";
            case "шумен", "shumen" -> "shumen/";
            case "ямполь", "yampol" -> "jampol/";
            case "банско", "bansko" -> "bansko/";
            case "бургас", "burgas" -> "burgas/";
            case "добрич", "dobrich" -> "dobrich/";
            case "смолян", "smolyan" -> "smolyan/";
            case "плевен", "pleven" -> "gr-pleven/";
            case "пловдив", "пловдів", "plovdiv" -> "plovdiv/";
            case "благоевград", "blagoevgrad" -> "blagoevgrad/";
            case "силистра", "сілістра", "silistra" -> "silistra/";
            default -> "bulgaria/";
        };
    }

    public static String getBG_bg(String workplace) {
        return  switch (workplace) {
            case "софия", "софія", "sofia" -> "гр.%20София";
            case "бургас", "burgas" -> "гр.%20Бургас";
            case "плевен", "pleven" -> "гр.%20Плевен";
            case "ямполь", "yampol" -> "гр.%20Ямполь";
            case "банско", "bansko" -> "гр.%20Банско";
            case "шумен", "shumen" -> "гр.%20Шумен";
            case "варна", "varna" -> "гр.%20Варна";
            case "русе", "rousse" -> "гр.%20Русе";
            case "ловеч", "lovech" -> "гр.%20Ловеч";
            case "добрич", "dobrich" -> "гр.%20Добрич";
            case "смолян", "smolyan" -> "гр.%20Смолян";
            case "пловдив", "пловдів", "plovdiv" -> "гр.%20Пловдив";
            case "благоевград", "blagoevgrad" -> "гр.%20Благоевград";
            case "силистра", "сілістра", "silistra" -> "гр.%20Силистра";
            default -> "";
        };
    }

    public static String getUK(String city){
        return  switch (city) {
            case "london", "лондон" -> "London";
            case "soho", "сохо" -> "Soho";
            case "avon", "ейвон" -> "Avon";
            case "glasgow","глазго" -> "Glasgow";
            case "leeds", "лідс","лидс" -> "Leeds";
            case "moorgate", "мургейт" -> "Moorgate";
            case "hampshire", "гемпшир" -> "Hampshire";
            case "berkshire", "беркшир" -> "Berkshire";
            case "yorkshire", "йоркшир" -> "Yorkshire";
            case "southwark", "саутворк" -> "Southwark";
            case "milton", "мілтон", "милтон" -> "Milton";
            case "manchester", "манчестер" -> "Manchester";
            case "aldersgate", "олдерсгейт" -> "Aldersgate";
            case "lanarkshire", "ланаркшир" -> "Lanarkshire";
            case "clerkenwell", "клеркенвелл" -> "Clerkenwell";
            case "lee bank","лі бенк", "ли бенк" -> "Lee Bank";
            case "bristol", "брістоль", "бристоль" -> "Bristol";
            case "shoreditch", "шордітч", "шордитч" -> "Shoreditch";
            case "birmingham","бірмінгем", "бирмингем" -> "Birmingham";
            case "westminster", "вестмінстер", "вестминстер" -> "Westminster";
            case "cambridgeshire", "кембріджшир", "кембриджшир" -> "Cambridgeshire";
            case "st james", "cент джеймс", "st_james", "cент_джеймс" -> "St James";
            case "nottinghamshire", "ноттінгемшир", "ноттингемшир" -> "Nottinghamshire";
            case "scotland", "шотландія", "шотландия" -> "Scotland";
            default -> "";
        };
    }

    public static String getDe(String city){
        return  switch (city) {
            case "дюссельдорф", "dusseldorf" -> "Dusseldorf";
            case "берлин", "berlin", "берлін" -> "Berlin";
            case "франкфурт", "frankfurt" -> "Frankfurt";
            case "karlsruhe", "карлсруе" -> "Karlsruhe";
            case "мангейм", "mannheim" -> "Mannheim";
            case "ганновер", "hanover" -> "Hanover";
            case "мюнхен", "munich" -> "Munich";
            default -> "Germany";
        };
    }

    public static String getAe(String city){
        return  switch (city) {
            case "дубай", "дубаи", "дубаі", "dubai" -> "Dubai";
            case "abu dhabi","абу-дабі", "абу-даби", "абу даби", "абу дабі" -> "Abu Dhabi";
            case "эль-айн", "эль айн", "ель-айн", "ель айн", "al ain" -> "Al Ain";
            case "шарджа",  "sharjah" -> "Sharjah";
            default -> "";
        };
    }

    public static String getFr(String city){
        return  switch (city) {
            case "париж", "paris" -> "Paris";
            case "тулуза", "toulouse" -> "Toulouse";
            case "ницца", "ніцца", "nice" -> "Nice";
            case "страсбург", "strasbourg" -> "Strasbourg";
            case "марсель", "marseille" -> "Marseille";
            case "монпелье", "монпельє", "montpellier" -> "Montpellier";
            case "лилль", "лілль", "lille" -> "Lille";
            case "бордо", "bordeaux" -> "Bordeaux";
            case "лион", "ліон", "lyon" -> "Lyon";
            case "нант", "nantes" -> "Nantes";
            case "ренн", "rennes" -> "Rennes";
            default -> "";
        };
    }

    public static String getIt(String city){
        return  switch (city) {
            case "рим", "roma" -> "Roma";
            case "турин", "torino" -> "Torino";
            case "генуя", "genova" -> "Genova";
            case "неаполь", "napoli" -> "Napoli";
            case "болонья", "bologna" -> "Bologna";
            case "палермо", "palermo" -> "Palermo";
            case "лацио", "лаціо", "lazio" -> "Lazio";
            case "милан", "мілан", "milano" -> "Milano";
            case "лигурия", "лігурія", "liguria" -> "Liguria";
            case "сицилия", "сицілія", "sicilia" -> "Sicilia";
            case "пьемонт", "пємонт", "piemonte" -> "Piemonte";
            case "флоренция", "флоренція", "firenze" -> "Firenze";
            case "кампания", "кампанія", "campania" -> "Campania";
            case "ломбардия", "ломбардія", "lombardia" -> "Lombardia";
            case "эмилия-романья", "эмілія-романья", "эмилия романья", "эмілія романья", "emilia-romagna" -> "Emilia-romagna";
            default -> "";
        };
    }

    public static String getFi(String city){
        return  switch (city) {
            case "хельсинки", "хельсінкі", "helsinki" -> "Helsinki";
            case "еспоо", "эспоо", "espoo" -> "Espoo";
            case "тампере", "tampere" -> "Tampere";
            case "вантаа", "vantaa" -> "Vantaa";
            case "турку", "turku" -> "Turku";
            default -> "";
        };
    }

    public static String getCh(String city){
        return  switch (city) {
            case "цюрих", "цюріх", "zürich" -> "Zürich";
            case "люцерн", "luzern" -> "Luzern";
            case "женева", "genève" -> "Genève";
            case "базель", "basel" -> "Basel";
            case "берн", "bern" -> "Bern";
            case "лозанна", "lausanne" -> "Lausanne";
            case "винтертур", "winterthur" -> "Winterthur";
            case "санкт-галлен", "санкт-гален", "санкт галлен", "санкт гален", "st. gallen" -> "St. Gallen";
            default -> "";
        };
    }

    public static String getSe(String city){
        return  switch (city) {
            case "стокгольм", "stockholm" -> "Stockholm";
            case "мальмё", "мальме", "malmö" -> "Malmö";
            case "гётеборг", "гетеборг", "gothenburg" -> "Gothenburg";
            default -> "";
        };
    }

    public static String getGr(String city){
        return  switch (city) {
            case "афины", "афіни", "athens" -> "Αθήνα";
            case "салоники", "салоніки", "thessaloniki"  -> "Θεσσαλονίκη";
            case "патры", "патри", "patras" -> "Πάτρα";
            case "пирей", "пірей", "piraeus" -> "Πειραιώς";
            case "перистери", "peristeri" -> "Περιστέρι";
            case "никея", "нікея", "nicaea" -> "Νίκαια";
            default -> "Ελλάδα";
        };
    }

    public static String getCityByCodeISOofCountry(String country, String workplace) {
        return switch (country) {
            case "ca" -> getCa(workplace);
            case "uk" -> getUK(workplace);
            case "bg" -> getBG_bg(workplace);
            case "pl" -> getPl(workplace);
            case "de" -> getDe(workplace);
            case "us" -> getUS(workplace);
            case "cz" -> getCZ(workplace);
            case "sk" -> getSK(workplace);
            case "ae" -> getAe(workplace);
            case "fr" -> getFr(workplace);
            case "it" -> getIt(workplace);
            case "fi" -> getFi(workplace);
            case "ch" -> getCh(workplace);
            case "se" -> getSe(workplace);
            case "gr" -> getGr(workplace);
            case "ua" -> getUA_ua(workplace);
            default -> !Pattern.compile("\\p{L1}").matcher(workplace).find() ? getJoobleUA(workplace) : getUpperStart(workplace);
        };
    }

    public static String getCodeISOByCity(String workplace) {
        return isMatches(of(citiesUA, uaAria), workplace) ? "ua" : isMatches(of(citiesUS, usAria), workplace) ? "us" :
               isMatches(of(citiesCa, caAria), workplace) ? "ca" : isMatches(of(citiesBg, bgAria), workplace) ? "bg" :
               isMatches(of(citiesDe, deAria), workplace) ? "de" : isMatches(of(citiesPl, plAria), workplace) ? "pl" :
               isMatches(of(citiesUK, ukAria), workplace) ? "uk" : isMatches(of(citiesIl, ilAria), workplace) ? "il" :
               isMatches(of(citiesSk, skAria), workplace) ? "sk" : isMatches(of(citiesCz, czAria), workplace) ? "cz" :
               isMatches(of(citiesAe, aeAria), workplace) ? "ae" : isMatches(of(citiesFr, frAria), workplace) ? "fr" :
               isMatches(of(citiesIt, itAria), workplace) ? "it" : isMatches(of(citiesFi, fiAria), workplace) ? "fi" :
               isMatches(of(citiesCh, chAria), workplace) ? "ch" : isMatches(of(citiesSe, seAria), workplace) ? "se" :
               isMatches(of(citiesGr, grAria), workplace) ? "gr" :
               isMatches(of(foreignAria), workplace) ? "foreign" : isMatches(of(remoteAria), workplace) ? "remote" : "all";
    }
}



