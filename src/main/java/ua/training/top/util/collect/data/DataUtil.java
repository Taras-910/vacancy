package ua.training.top.util.collect.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.maxLengthText;

public class DataUtil {
    private static StringBuilder builder;

    public static String getJoin(String ...text) {
        if (builder == null) {
            builder = new StringBuilder(1024);
        }
        //https://stackoverflow.com/questions/5192512/how-can-i-clear-or-empty-a-stringbuilder
        builder.setLength(0);
        Arrays.stream(text).forEach(s -> builder.append(s));
        return builder.toString();
    }

    public static final String
            link = "see the card",
            monetary_amount_regex = "[-–—k(дот-]",
            document_url = "GetDocument url={}\n",
            document_user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.1.2 Safari/605.1.15",
            internet_connection_error = "There may be no internet connection or exception={} by url={} ",
            finish_message = "\nfinish is ok,\ntime: {}ms\ncreated: {}\nupdated: {}\nFreshen: {}\n" + ":".repeat(120),
            error = "There is error \ne={}\n for parse \n{}",
            common_number_vacancyTos = "Common number vacancyTos = {}",
            get_vacancy = "GetVacancies workplace={} language={}",
            error_select = "Select error e {}",
            jobcareer = "JobCareerStrategy", work = "WorkStrategyStrategy", rabota = "RabotaStrategy",
            jobs = "jobsStrategy", djinni = "DjinniStrategy", nofluff = "NofluffjobsStrategy", jobBank = "jobBankStrategy",
            jooble = "joobleStrategy", linkedin = "linkedinStrategy", indeed_ca = "CaIndeedStrategy",
            zaplata = "zaplataStrategy", jobsmarket = "jobsmarketStrategy", indeed = "UAIndeedStrategy", cwjobs = "CwJobsStrategy",
            itJob = "itJobStrategy", jabsBG = "JobsBGStrategy", itJobsWatch = "ItJobsWatchStrategy", reed = "ReedStrategy",
            local_date = "releaseDate", age_field = "age", address_field = "address", month = "month", middle = "middle",
            trainee = "trainee", junior = "junior", senior = "senior", expert = "expert";

    public static final List<String>
            usdAria = of("usd", "$", "us$"),
            eurAria = of("eur", "€"),
            plnAria = of("pln", "zł", "₲"),
            czeAria = of("₭", "kč", "₭č"),
            gbrAria = of("gbp", "£", "₤"),
            kztAria = of("kzt", "тг", "₸"),
            cadAria = of("cad", "ca$", "c$", "₡"),
            hrnAria = of("hrn", "uah", "грн", "₴"),
            rubAria = of("rub", "rur", "руб", "₽"),
            bynAria = of("бел. руб", "бел руб", "br", "byn", "byr", "₱"),
            bgnAria = of("bgn", "bg", "lev", "лев", "лв.", "₾"),
            allSalaries = of("грн", "uah", "hrn", "₴", "$", "usd", "eur", "€", "pln", "zł", "₲", "gbp", "£", "₤", "₱",
                    "бел. руб", "бел руб", "руб", "₽", "kzt", "тг", "₸", "br", "byn", "cad", "ca$", "c$", "₡","₭","kč",
                    "bgn", "bg", "lev", "лев", "лв.", "₾"),
            wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+", "[^\\d*]"),

            yearAria = of("год", "рік", "year", "annually", "annum", "annual", "rok", "jahr", "година"),
            dayAria = of("день", "дня", "day", "daily", "дней", "days", "today", "tag", "tage", "tagen", "днів", "дні", "сьогодні", "сегодня", "dzień", "dni"),
            hourAria = of("час", "годину", "hour", "hourly", "/h", "stunden", "годину", "години", "годин", "часа", "часов", "hours", "stunden", "std.", "godzina", "godziny"),
            monthsOfYearAria = of("січня", "января", "январь", "лютого", "февраля", "февраль", "березня", "марта", "март",
                    "квітня", "апреля", "апрель", "травня", "мая", "май", "червня", "июня", "июнь", "липня", "июля",
                    "июль", "серпня", "августа", "август", "вересня", "сентября", "сентябрь", "жовтня", "октября",
                    "октябрь", "листопада", "ноября", "ноябрь", "грудня", "декабря", "декабрь", "jan", "feb", "mar",
                    "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", "january", "february", "march",
                    "april", "may", "june", "july", "august", "september", "october", "november", "december"),
            wasteSkills = of("продав", "бармен", "ресто", "водитель такси", "виграй"),
            workersIT = of("engineer", "фронт", "front", "бэк", "backend", "web", "веб", "фулстек", "microservice",
                    "микросервис", "програм", "program", "git", "spring", "maven", "develop", "sql", "docker", "postgre",
                    "rest", "mvc", "jpa", "pattern"),
            traineeAria = of("intern", "trainee", "интерн", "стажировка", "internship", "стажер", "стажист"),
            levelAria = of("intern", "trainee", "интерн", "стажировка", "internship", "стажер", "стажист", "junior",
                    "младший", "без опыта", "обучение", "middle", "средний", "мідл", "senior", "старший", "expert",
                    "lead", "тимлид", "ведущий", "team lead"),

            uaAria = of("ukraine", "украина", "україна", "ua"),
            citiesUA = of("київ", "киев", "дніпро", "днепр", "dnipro", "харків", "харьков", "kharkiv", "львів", "львов",
                    "mykolaiv", "одесса", "odesa", "одеса", "винница", "vinnitsia", "вінниця", "запоріжжя", "запорожье",
                    "zaporizhzhya", "chernivtsi", "чернівці", "черновцы", "чернігів", "чернигов", "chernigiv", "миколаїв",
                    "івано-франківськ", "ужгород", "ивано-франковск", "ivano-frankivsk", "луцк", "луцьк", "lutsk",
                    "кіровоград", "николаев", "жовті-води", "кривой-рог", "кривий-ріг", "желтые-воды", "ужгород","lviv",
                    "uzhgorod", "каменец-подольский", "камянець-подільський", "тернопіль", "тернополь", "ternopil",
                    "кропивницкий", "кропивницький", "кировоград", "кіровоград", "kropyvnytskyi", "kirovograd",
                    "черкаси", "черкассы", "полтава", "poltava", "хмельницький", "khmelnitsky", "рівне", "роно", "rivne",
                    "rovno", "житомир", "zhytomyr"),
            plAria = of("польша", "польща", "poland", "polski", "pol"),
            citiesPl = of("lodz", "варшава", "warszawa", "krakow", "краков", "kraków", "wroclaw", "вроцлав", "gdansk",
                    "гданськ", "pol", "гданск", "poznan", "познань", "poznań", "katowice", "катовіце", "катовице",
                    "лодзь", "gdynia"),
            deAria = of("germany", "германия", "німеччина"),
            citiesDe = of("берлін", "берлин", "berlin", "мангейм", "mannheim", "гамбург", "hamburg", "ганновер",
                    "hanover", "дюссельдорф", "dusseldorf", "мюнхен", "munich", "франкфурт-на-майне", "frankfurt am main",
                    "karlsruhe", "карлсруе", "кёльн", "köln", "штутгарт", "stuttgart", "дюссельдорф", "düsseldorf",
                    "лейпциг", "leipzig", "лейпціг", "дортмунд", "dortmund", "єссен", "essen", "бремен", "bremen",
                    "дрезден", "dresden", "нюрнберг", "nürnberg", "дуйсбург", "duisburg", "бохум", "bochum", "вупперталь",
                    "wuppertal", "билефельд", "білефельд", "bielefeld", "бонн", "bonn", "мюнстер", "münster", "аугсбург",
                    "augsburg", "висбаден", "вісбаден", "wiesbaden", "гельзенкирхен", "гельзенкірхен", "gelsenkirchen",
                    "мёнхенгладбах", "mönchengladbach", "брауншвейг", "braunschweig"),
            bgAria = of("bulgaria", "болгария", "болгарія"),
            citiesBg = of("софия", "софія", "sofia",  "варна", "varna", "пловдив", "пловдів", "plovdiv", "бургас",
                    "burgas", "русе", "rousse", "плевен", "pleven", "шумен", "shumen", "ямполь", "yampol", "добрич",
                    "dobrich", "банско", "bansko", "силистра", "сілістра", "silistra", "ловеч", "lovech", "смолян",
                    "smolyan", "благоевград", "bulgaria", "болгария", "blagoevgrad"),
            caAria = of("canadа", "канада", "canad", "канад"),
            citiesCa = of("toronto", "торонто", "calgary", "калгарі", "калгари", "ontario", "онтарио", "онтаріо",
                    "edmonton", "едмонтон", "эдмонтон", "vancouver", "ванкувер", "brampton", "брэмптон", "бремптон",
                    "ottawa", "оттава", "mississauga", "миссиссога", "міссісога", "британська колумбія",
                    "британская колумбия", "british columbia", "альберта", "alberta", "манитоба", "манітоба", "manitoba",
                    "montréal", "montreal", "монреаль", "quebec", "квебек", "hamilton", "гамильтон", "гамільтон",
                    "victoria", "виктория", "вікторія", "winnipeg", "виннипег", "вінніпег"),
            ukAria = of("англія", "англия", "england", "united kingdom"),
            citiesUK = of("st james", "moorgate", "shoreditch", "soho", "southwark", "aldersgate", "lee bank",
                    "westminster", "milton", "clerkenwell", "bristol", "leeds", "birmingham", "glasgow", "london",
                    "manchester", "avon", "yorkshire", "cambridgeshire", "lanarkshire", "hampshire", "berkshire",
                    "nottinghamshire", "yorkshire", "scotland", "cент джеймс", "клеркенвелл", "мургейт", "шордітч",
                    "шордитч", "сохо", "саутворк", "олдерсгейт", "лі бенк", "ли бенк", "бірмінгем", "вестмінстер",
                    "мілтон", "вестминстер", "милтон",  "брістоль", "бристоль", "лидс", "бирмингем", "uk", "глазго",
                    "лондон", "манчестер", "ейвон", "йоркшир", "кембріджшир", "кембриджшир", "лідс", "гемпшир",
                    "ланаркшир", "беркшир", "ноттінгемшир", "ноттингемшир", "йоркшир", "шотландія", "шотландия"),
            ilAria = of("israel", "израиль", "ізраїль", "ישראל"),
            citiesIl = of("тель-авив", "тель авив",
                    "tel aviv", "תל אביב", "хайфа", "haifa" , "חיפה", "ришон-ле-цион", "ришон ле цион", "rishon lezion",
                    "ראשון לציון", "петах-тиква", "петах тиква", "petah-tikva", "פתח-תקווה", "ашдод", "ashdod", "אשדוד",
                    "нетания", "netanya", "נתניה", "беэр-шева", "беэр шева", "beersheba", "באר שבע", "бней-брак", "бней брак",
                    "bnei marriage", "נישואי בני", "холон", "holon", "рамат-ган", "рамат ган", "ramat-gan", "רמת-גן",
                    "реховот", "rehovot", "רחובות", "ашкелон", "ashkelon", "אשקלון", "бат-ям", "бат ям", "bat-yam", "בת-ים"),
            usAria = of("сша", "америка", "usa", "united states of america", "america"),
            citiesUS = of("нью-йорк", "new york", "лос-анджелес", "лос анджелес",
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
                    "балтімор", "baltimore", "сакраменто", "sacramento"),
            foreignAria = of("другие страны", "foreign", "за_рубежем", "за рубежом", "за рубежем", "за кордоном"),
            remoteAria = of("remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота",
                    "дистанционна работа"),
            otherAria = of("france", "франция", "франція", "франкфурт", "frankfurt", "israel", "израиль", "ізраїль",
                    "minsk", "минск", "мінськ", "ізраїль", "израиль", "israel", "швейцарія", "швейцария", "switzerland",
                    "france", "франція", "франция", "italy", "італія", "италия", "турція", "турция", "turkey", "англія",
                    "англия", "england", "uk", "united kingdom", "канада", "canada", "польща", "польша", "poland", "pol",
                    "czechia", "чехія", "чехия", "швеція", "швеция", "sweden", "фінляндія", "финляндия", "finland",
                    "норвегія", "норвегия", "norway", "сінгапур", "singapore", "німеччина", "germany", "германия", "оаэ",
                    "эмираты", "emirates", "австралія", "австралия", "australia", "філіппіни", "филиппины", "philippines",
                    "естонія", "эстония", "estonia", "netherlands", "голландия", "нідерланди", "білорусь", "беларусь",
                    "киргизстан", "казахстан", "узбекистан", "молдова", "іран", "иран", "iran", "азербайджан", "армения",
                    "болгария", "bulgaria", "черногория", "сша", "турция", "турція", "turkey", "латвия", "латвія",
                    "latvia", "литва", "lithuania", "дания", "данія", "denmark", "бельгия", "бельгія", "belgium",
                    "словакия", "словакія", "slovakia", "греция", "греція", "greece", "кипр", "кіпр", "cyprus"),
            citiesRU = of("russia", "россия", "росія", "ru", "st petersburg", "санкт петербург", "санкт-петербург","spb",
                    "москва", "moskow", "msk", "новосибирск", "екатеринбург", "томск", "краснодар", "пермь", "ульяновск",
                    "ростов-на-дону", "нижний новгород", "казань", "самара", "воронеж");

    public static boolean isMatch(List<String> area, String text) {
        return area.stream().anyMatch(a -> text.toLowerCase().indexOf(a) != -1);
    }

    public static boolean isMatch(List<String> list1, List<String> list2, List<String> list3, String text) {
        List<String> list = new ArrayList(list1);
        list.addAll(list2);
        list.addAll(list3);
        return isMatch(list, text);
    }

    public static boolean isMatch(List<String> list1, List<String> list2, String text) {
        List<String> list = new ArrayList(list1);
        list.addAll(list2);
        return isMatch(list, text);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.trim().equals("•");
    }

    public static String getLinkIfEmpty(String text) {
        return isEmpty(text) ? link : text;
    }

    public static String getUpperStart(String text) {
        return !isEmpty(text) && text.length() > 1 ? getJoin(text.substring(0, 1).toUpperCase(),text.substring(1)) : link;
    }

    public static String getReplace(String text, List<String> words, String replacement) {
        for (String w : words) {
            text = text.replaceAll(w, replacement).trim();
        }
        return text;
    }

    public static String getToTitle(String title) {
        return isEmpty(title) ? link : getUpperStart(correctJavaScript(title));
    }

    public static String getToName(String compName) {
        return isEmpty(compName) ? link : compName.indexOf(",") != -1 ? compName.split(",")[0].trim() : compName;
    }

    public static String getToSkills(String skills) {
        if (isEmpty(skills)) {
            return link;
        }
        skills = skills.indexOf("Experience level:") != -1 ? skills.substring(skills.indexOf("Experience level:")) : skills;
        return correctJavaScript(skills.length() > maxLengthText ? skills.substring(0, maxLengthText) : skills);
    }

    public static String correctJavaScript(String text) {
        return text.indexOf("Java Script") != -1 ? text.replaceAll("Java Script", "JavaScript") : text;
    }
}

    /*    public static final List<String>
            usdAria = of("usd", "$", "us$"),
            eurAria = of("eur", "€"),
            plnAria = of("pln", "zł", "₲"),
            czeAria = of("₭", "kč", "₭č"),
            gbrAria = of("gbp", "£", "₤"),
            kztAria = of("kzt", "тг", "₸"),
            cadAria = of("cad", "ca$", "c$", "₡"),
            hrnAria = of("hrn", "uah", "грн", "₴"),
            rubAria = of("rub", "rur", "руб", "₽"),
            bynAria = of("бел. руб", "бел руб", "br", "byn", "byr", "₱"),
            bgnAria = of("bgn", "bg", "lev", "лев", "лв.", "₾"),
            allSalaries = of("грн", "uah", "hrn", "₴", "$", "usd", "eur", "€", "pln", "zł", "₲", "gbp", "£", "₤", "₱",
                    "бел. руб", "бел руб", "руб", "₽", "kzt", "тг", "₸", "br", "byn", "cad", "ca$", "c$", "₡","₭","kč",
                    "bgn", "bg", "lev", "лев", "лв.", "₾"),
            wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+", "[^\\d*]"),

    yearAria = of("год", "рік", "year", "annually", "annum", "annual", "rok", "jahr", "година"),
            dayAria = of("день", "дня", "day", "daily", "дней", "days", "today", "tag", "tage", "tagen", "днів", "дні", "сьогодні", "сегодня", "dzień", "dni"),
            hourAria = of("час", "годину", "hour", "hourly", "/h", "stunden", "годину", "години", "годин", "часа", "часов", "hours", "stunden", "std.", "godzina", "godziny"),
            monthsOfYearAria = of("січня", "января", "январь", "лютого", "февраля", "февраль", "березня", "марта", "март",
                    "квітня", "апреля", "апрель", "травня", "мая", "май", "червня", "июня", "июнь", "липня", "июля",
                    "июль", "серпня", "августа", "август", "вересня", "сентября", "сентябрь", "жовтня", "октября",
                    "октябрь", "листопада", "ноября", "ноябрь", "грудня", "декабря", "декабрь", "jan", "feb", "mar",
                    "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", "january", "february", "march",
                    "april", "may", "june", "july", "august", "september", "october", "november", "december"),
            wasteSkills = of("продав", "бармен", "ресто", "водитель такси", "виграй"),
            workersIT = of("engineer", "фронт", "front", "бэк", "backend", "web", "веб", "фулстек", "microservice",
                    "микросервис", "програм", "program", "git", "spring", "maven", "develop", "sql", "docker", "postgre",
                    "rest", "mvc", "jpa", "pattern"),

    traineeAria = of("intern", "trainee", "интерн", "internship", "стажировка", "стажер", "стажист"),
            juniorAria = of("junior", "младший", "без опыта", "обучение"), middleAria = of("middle", "средний", "мідл"),
            seniorAria = of("senior", "старший"), expertAria = of("expert", "lead", "team lead", "ведущий", "тимлид"),

    uaAria = of("ukraine", "украина", "україна", "ua"),
            kievAria = of("kyiv", "kiev", "київ", "киев"), dniproAria = of("дніпро", "днепр", "dnipro"),
            kharkivAria = of("харків", "харьков", "kharkiv"), lvivAria = of("львів", "львов", "lviv"),
            uzhgorodAria = of("ужгород", "uzhgorod"),odesaAria = of("одесса", "odesa", "одеса"),
            mykolaivAria = of("mykolaiv", "миколаїв", "николаев"), vinnitsiaAria = of("винница", "vinnitsia", "вінниця"),
            chernigivAria = of("чернігів", "чернигов", "chernigiv"), chernivtsiAria = of("chernivtsi", "чернівці", "черновцы"),
            zaporizhzhyaAria = of("запоріжжя", "запорожье", "zaporizhzhya"),
            ivano_frankivskAria = of("івано-франківськ", "ивано-франковск", "ivano-frankivsk"),
            citiesUA = of("ukraine", "ua", "украина", "україна", "kyiv", "kiev", "київ", "киев", "дніпро", "днепр",
                    "dnipro", "харків", "харьков", "kharkiv", "львів", "львов", "lviv", "mykolaiv", "одесса", "odesa",
                    "одеса", "винница", "vinnitsia", "вінниця", "запоріжжя", "запорожье", "zaporizhzhya", "chernivtsi",
                    "чернівці", "черновцы", "чернігів", "чернигов", "chernigiv", "івано-франківськ", "ужгород",
                    "ивано-франковск", "ivano-frankivsk", "луцк", "луцьк", "миколаїв", "николаев", "жовті-води",
                    "кривой-рог", "кривий-ріг", "желтые-воды", "каменец-подольский", "камянець-подільський", "тернопіль",
                    "тернополь", "кропивницкий", "кропивницький", "кировоград", "кіровоград"),
            plAria = of("польша", "польща", "poland", "polski", "pol"),
            poznanAria = of("poznan", "познань"), gdanskAria = of("gdansk", "гданськ", "гданск"),
            krakowAria = of("krakow", "краков", "краків"),
            warszawaAria = of("варшава", "warszawa"), wroclawAria = of("wroclaw", "вроцлав"),
            citiesPl = of("варшава", "warszawa", "krakow", "краков",
                    "kraków", "wroclaw", "вроцлав", "gdansk", "гданськ", "гданск", "poznan", "познань", "poznań",
                    "katowice", "катовіце", "катовице", "lodz", "лодзь", "gdynia"),
            deAria = of("germany", "германия", "німеччина"),
            berlinAria = of("берлин", "berlin", "берлін"), mannheimAria = of("мангейм", "mannheim"),
            hamburgAria = of("гамбург", "hamburg"), hanoverAria = of("ганновер", "hanover"),
            dusseldorfAria = of("дюссельдорф", "dusseldorf"), munichAria = of("мюнхен", "munich"),
            frankfurtAria = of("франкфурт", "frankfurt"), karlsruheAria = of("karlsruhe", "карлсруе"),
            citiesDe = of("берлін", "берлин", "berlin", "мангейм", "mannheim", "гамбург", "hamburg", "ганновер", "hanover",
                    "дюссельдорф", "dusseldorf", "мюнхен", "munich", "франкфурт", "frankfurt", "karlsruhe", "карлсруе"),
            bgAria = of("bulgaria", "болгария", "болгарія"),
            sofiaAria = of("sofia", "софия", "софія"), varnaAria = of("varna", "варна"),
            plovdivAria = of("пловдив", "пловдів", "plovdiv"), rousseAria = of("русе", "rousse"),
            plevenAria = of("плевен", "pleven"), shumenAria = of("шумен", "shumen"), yampolAria = of("ямполь", "yampol"),
            dobrichAria = of("добрич", "dobrich"), banskoAria = of("банско", "bansko"), burgasAria = of("burgas", "бургас"),
            silistraAria = of("силистра", "сілістра", "silistra"), lovechAria = of("ловеч", "lovech"),
            smolyanAria = of("смолян", "smolyan"),blagoevgradAria = of("благоевград", "blagoevgrad"),
            citiesBg = of("софия", "софія", "sofia",  "варна", "varna", "пловдив", "пловдів", "plovdiv", "бургас",
                    "burgas", "русе", "rousse", "плевен", "pleven", "шумен", "shumen", "ямполь", "yampol", "добрич",
                    "dobrich", "банско", "bansko", "силистра", "сілістра", "silistra", "ловеч", "lovech", "смолян",
                    "smolyan", "благоевград", "bulgaria", "болгария", "blagoevgrad"),
            caAria = of("canadа", "канада", "canad", "канад"),
            ontarioAria = of("ontario", "онтарио"), albertaAria = of("альберта", "alberta"),
            vancouverAria = of("vancouver", "ванкувер"), montrealAria = of("montréal", "montreal", "монреаль"),
            bramptonAria = of("brampton", "брамптон"), victoriaAria = of("victoria", "виктория"),
            ottawaAria = of("ottawa", "оттава"), hamiltonAria = of("hamilton", "гамильтон"),
            calgaryAria = of("calgary", "калгарі", "калгари"), winnipegAria = of("winnipeg", "виннипег"),
            edmontonAria = of("edmonton", "едмонтон", "эдмонтон"), quebecAria = of("quebec", "квебек"),
            mississaugaAria = of("mississauga", "миссиссога", "міссісога"), torontoAria = of("toronto", "торонто"),
            british_columbiaAria = of("британська колумбія", "британская колумбия", "british columbia"),
            manitobaAria = of("манитоба", "манітоба", "manitoba"), scotlandAria = of("шотландія", "шотландия"),
            citiesCa = of("toronto", "торонто", "calgary", "калгарі", "калгари",
                    "ontario", "онтарио", "онтаріо", "edmonton", "едмонтон", "эдмонтон", "vancouver", "ванкувер",
                    "brampton", "брэмптон", "бремптон", "ottawa", "оттава", "mississauga", "миссиссога", "міссісога",
                    "британська колумбія", "британская колумбия", "british columbia", "альберта", "alberta", "манитоба",
                    "манітоба", "manitoba", "montréal", "montreal", "монреаль", "quebec", "квебек", "hamilton",
                    "гамильтон", "гамільтон","victoria", "виктория", "вікторія", "winnipeg", "виннипег", "вінніпег"),
            ukAria = of("англія", "англия", "england", "united kingdom"),
            st_jamesAria = of("st james", "cент джеймс"),
            moorgateAria = of("moorgate", "мургейт"), shoreditchAria = of("shoreditch", "шордітч", "шордитч"),
            southwarkAria = of("southwark", "саутворк"), berkshireAria = of("berkshire", "беркшир"),
            aldersgateAria = of("aldersgate", "олдерсгейт"), lee_bank = of("lee bank", "лі бенк", "ли бенк", "lee_bank"),
            westminsterAria = of("westminster", "вестмінстер", "вестминстер"), miltonAria = of("milton", "мілтон", "милтон"),
            clerkenwellAria = of("clerkenwell", "клеркенвелл"), bristolAria = of("bristol", "брістоль", "бристоль"),
            leedsAria = of("leeds", "лидс", "лідс"), birminghamAria = of("birmingham", "бірмінгем", "бирмингем"),
            glasgowAria = of("glasgow", "глазго"), londonAria = of("london", "лондон"), avonAria = of("avon", "ейвон"),
            manchesterAria = of("manchester", "манчестер"), yorkshireAria = of("yorkshire", "йоркшир"),
            cambridgeshireAria = of("cambridgeshire", "кембріджшир", "кембриджшир"), sohoAria = of("soho", "сохо"),
            lanarkshireAria = of("lanarkshire", "ланаркшир"), hampshireAria = of("hampshire", "гемпшир"),
            nottinghamshireAria = of("nottinghamshire", "ноттінгемшир", "ноттингемшир"),
            citiesUK = of("англія", "англия", "england", "united kingdom", "st james", "moorgate", "shoreditch", "soho",
                    "southwark", "aldersgate", "lee bank", "westminster", "milton", "clerkenwell", "bristol", "leeds",
                    "birmingham", "glasgow", "london", "manchester", "avon", "yorkshire", "cambridgeshire", "lanarkshire",
                    "hampshire", "berkshire", "nottinghamshire", "yorkshire", "scotland", "cент джеймс", "клеркенвелл",
                    "мургейт", "шордітч", "шордитч", "сохо", "саутворк", "олдерсгейт", "лі бенк", "ли бенк", "бірмінгем",
                    "вестмінстер", "мілтон", "вестминстер", "милтон",  "брістоль", "бристоль", "лидс", "бирмингем", "uk",
                    "глазго", "лондон", "манчестер", "ейвон", "йоркшир", "кембріджшир", "кембриджшир", "лідс", "гемпшир",
                    "ланаркшир", "беркшир", "ноттінгемшир", "ноттингемшир", "йоркшир", "шотландія", "шотландия"),
            otherAria = of("foreign", "ізраїль", "израиль", "israel", "швейцарія", "швейцария", "switzerland",
                    "france", "франція", "франция", "italy", "італія", "италия", "турція", "турция", "turkey", "англія",
                    "англия", "england", "uk", "united kingdom", "канада","canada", "польща", "польша", "poland", "pol",
                    "czechia", "чехія", "чехия", "швеція", "швеция", "sweden", "фінляндія", "финляндия", "finland",
                    "норвегія", "норвегия", "norway", "сінгапур", "singapore", "німеччина", "germany", "германия", "оаэ",
                    "эмираты", "emirates", "австралія", "австралия", "australia", "філіпіни", "филипины", "philippines",
                    "естонія", "эстония", "estonia", "netherlands", "голландия", "нідерланди", "білорусь", "беларусь",
                    "киргизстан", "казахстан", "узбекистан", "молдова", "іран", "иран", "iran", "азербайджан", "армения",
                    "болгария", "bulgaria", "черногория", "сша", "турция", "турція", "turkey", "латвия", "латвія",
                    "latvia", "литва", "lithuania", "дания",  "данія", "denmark", "бельгия",  "бельгія", "belgium",
                    "словакия", "словакія", "slovakia", "греция", "греція", "greece", "кипр", "кіпр", "cyprus"),
            minskAria = of("минск", "minsk", "мінськ"),
            foreignAria = of("другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном"),
            remoteAria = of("remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота",
                    "дистанционна работа"),
            frAria = of("france", "франция", "франція"),
            israelAria = of("israel", "израиль", "ізраїль"),
            citiesRU = of("russia", "россия", "росія", "ru", "st petersburg", "санкт петербург", "санкт-петербург","spb",
                    "москва", "moskow", "msk", "новосибирск", "екатеринбург", "томск", "краснодар", "пермь", "ульяновск",
                    "ростов-на-дону", "нижний новгород", "казань", "самара", "воронеж");*/
