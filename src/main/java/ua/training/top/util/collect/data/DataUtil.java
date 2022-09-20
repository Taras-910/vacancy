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
            is_date_number = "\\d{1,2}",
            is_number_format = "[\\d\\.]+",
            is_kilo = ".*\\d[\\d\\.-]+k.*",
            is_date_numbers = "^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])(t\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})?$",
            extract_salary = "((?:[\\d,\\.[–до\\-k-  ]+\\s  &nbsp]+\\b)(\\s*)?[  ]?(\\p{Sc}|ƒ))|(" +
                    "(?:\\p{Sc}|ƒ)(\\s*)?[  ]?[\\d,\\.[–до\\-k-  ]\\s  &nbsp]+\\b)",
            extract_month = "(?:\\s?\\d?\\d)\\s?\\(?\\s?([\\(месяцева])+\\.*",
            extract_age = "(?:[1-7]\\d)\\s([годалетрківи])+",
            extract_address = "(?:[а-яА-ЯіїєA-Za-z,\\s·]+)\\b",
            extract_date = "((?:\\d){1,2}\\s([а-яі])+|^[а-яі]{3,11})|((?:[A-Za-z])+\\s+(\\d){1,2})",
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
            zaplata = "zaplataStrategy", jobsmarket = "jobsmarketStrategy", indeed = "UAIndeedStrategy",
            itJob = "itJobStrategy", jabsBG = "JobsBGStrategy",
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
            yearAria = of("год", "рік", "year", "annually"),
            dayAria = of("день", "day"),
            hourAria = of("час", "годину", "hour", "hourly", "/h"),
            wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+", "[^\\d*]"),
            traineeAria = of("intern", "trainee", "интерн", "internship", "стажировка", "стажер", "стажист"),
            juniorAria = of("junior", "младший", "без опыта", "обучение"),
            middleAria = of("middle", "средний", "мідл"),
            seniorAria = of("senior", "старший"),
            expertAria = of("expert", "lead", "team lead", "ведущий", "тимлид"),
            kievAria = of("kyiv", "kiev", "київ", "киев"),
            ukraineAria = of("ukraine", "украина", "україна", "ua"),
            dniproAria = of("дніпро", "днепр", "dnipro"),
            kharkivAria = of("харків", "харьков", "kharkiv"),
            lvivAria = of("львів", "львов", "lviv"),
            uzhgorodAria = of("ужгород", "uzhgorod"),
            odesaAria = of("одесса", "odesa", "одеса"),
            mykolaivAria = of("mykolaiv", "миколаїв", "николаев"),
            vinnitsiaAria = of("винница", "vinnitsia", "вінниця"),
            chernigivAria = of("чернігів", "чернигов", "chernigiv"),
            chernivtsiAria = of("chernivtsi", "чернівці", "черновцы"),
            zaporizhzhyaAria = of("запоріжжя", "запорожье", "zaporizhzhya"),
            ivano_frankivskAria = of("івано-франківськ", "ивано-франковск", "ivano-frankivsk"),
            polandAria = of("польша", "польща", "poland", "polski", "pol"),
            gdanskAria = of("gdansk", "гданськ", "гданск"),
            krakowAria = of("krakow", "краков", "краків"),
            warszawaAria = of("варшава", "warszawa"),
            wroclawAria = of("wroclaw", "вроцлав"),
            poznanAria = of("poznan", "познань"),
            sofiaAria = of("sofia", "софия", "софія"),
            varnaAria = of("varna", "варна"),
            burgasAria = of("burgas", "бургас"),
            minskAria = of("минск", "minsk", "мінськ"),
            berlinAria = of("берлин", "berlin", "берлін"),
            germanyAria = of("germany", "германия", "німеччина"),
            franceAria = of("france", "франция", "франція"),
            israelAria = of("israel", "израиль", "ізраїль"),
            vancouverAria = of("vancouver", "ванкувер"),
            montrealAria = of("montréal", "монреаль"),
            torontoAria = of("toronto", "торонто"),
            ontarioAria = of("ontario", "онтарио"),
            quebecAria = of("quebec", "квебек"),
            bramptonAria = of("brampton", "брамптон"),
            victoriaAria = of("victoria", "виктория"),
            ottawaAria = of("ottawa", "оттава"),
            hamiltonAria = of("hamilton", "гамильтон"),
            winnipegAria = of("winnipeg", "виннипег"),
            foreignAria = of("другие страны", "foreign", "за_рубежем", "за рубежом", "за кордоном"),
            remoteAria = of("remote", "relocate", "релокейт", "удаленно", "віддалено", "віддалена робота",
                    "дистанционна работа"),
            citiesUA = of("ukraine", "ua", "украина", "україна", "kyiv", "kiev", "київ", "киев", "дніпро", "днепр",
                    "dnipro", "харків", "харьков", "kharkiv", "львів", "львов", "lviv", "mykolaiv", "одесса", "odesa",
                    "одеса", "винница", "vinnitsia", "вінниця", "запоріжжя", "запорожье", "zaporizhzhya", "chernivtsi",
                    "чернівці", "черновцы", "чернігів", "чернигов", "chernigiv", "івано-франківськ", "ужгород",
                    "ивано-франковск", "ivano-frankivsk", "луцк", "луцьк", "миколаїв", "николаев", "жовті-води",
                    "кривой-рог", "кривий-ріг", "желтые-воды", "каменец-подольский", "камянець-подільський", "тернопіль",
                    "тернополь", "кропивницкий", "кропивницький", "кировоград", "кіровоград"),
            citiesRU = of("russia", "россия", "росія", "ru", "st petersburg", "санкт петербург", "санкт-петербург","spb",
                    "москва", "moskow", "msk", "новосибирск", "екатеринбург", "томск", "краснодар", "пермь", "ульяновск",
                    "ростов-на-дону", "нижний новгород", "казань", "самара", "воронеж"),
            citiesPL = of("poland", "польща", "польша", "polski", "pol", "варшава", "warszawa", "krakow", "краков",
                    "kraków", "wroclaw", "вроцлав", "gdansk", "гданськ", "гданск", "poznan", "познань", "poznań",
                    "katowice", "катовіце", "катовице", "lodz", "лодзь", "gdynia"),
            citiesDe = of("берлін", "берлин", "berlin", "мангейм", "mannheim", "гамбург", "hamburg", "ганновер", "hanover",
                    "дюссельдорф", "dusseldorf", "мюнхен", "munich", "франкфурт", "frankfurt"),
            citiesBg = of("софия", "софія", "sofia",  "варна", "varna", "пловдив", "пловдів", "plovdiv", "бургас",
                    "burgas", "русе", "ruse", "плевен", "pleven", "шумен", "shumen", "ямполь", "yampol", "добрич",
                    "dobrich", "банско", "bansko", "силистра", "сілістра", "silistra", "ловеч", "lovech", "смолян",
                    "smolyan", "благоевград", "bulgaria", "болгария", "blagoevgrad"),
            citiesCanada = of("canadа", "канада", "canad", "канад", "toronto", "торонто", "calgary", "калгарі", "калгари",
                    "ontario", "онтарио", "онтаріо", "edmonton", "едмонтон", "эдмонтон", "vancouver", "ванкувер",
                    "brampton", "брэмптон", "бремптон", "ottawa", "оттава", "mississauga", "миссиссога", "міссісога",
                    "британська колумбія", "британская колумбия", "british columbia", "альберта", "alberta", "манитоба",
                    "манітоба", "manitoba", "montréal", "montreal", "монреаль", "quebec", "квебек", "hamilton",
                    "гамильтон", "гамільтон","victoria", "виктория", "вікторія", "winnipeg", "виннипег", "вінніпег"),
            countriesOfWorld = of("foreign", "ізраїль", "израиль", "israel", "швейцарія", "швейцария", "switzerland",
                    "france", "франція", "франция", "italy", "італія", "италия", "турція", "турция", "turkey", "англія",
                    "англия", "england", "uk", "united kingdom", "канада","canada", "польща", "польша", "poland", "pol",
                    "czechia", "чехія", "чехия", "швеція", "швеция", "sweden", "фінляндія", "финляндия", "finland",
                    "норвегія", "норвегия", "norway", "сінгапур", "singapore", "німеччина", "germany", "германия", "оаэ",
                    "эмираты", "emirates", "австралія", "австралия", "australia", "філіпіни", "филипины", "philippines",
                    "естонія", "эстония", "estonia", "netherlands", "голландия", "нідерланди", "білорусь", "беларусь",
                    "киргизстан", "казахстан", "узбекистан", "молдова", "іран", "иран", "iran", "азербайджан", "армения",
                    "болгария", "bulgaria"),
            monthsOfYearAria = of("січня", "января", "январь", "лютого", "февраля", "февраль", "березня", "марта", "март",
                    "квітня", "апреля", "апрель", "травня", "мая", "май", "червня", "июня", "июнь", "липня", "июля",
                    "июль", "серпня", "августа", "август", "вересня", "сентября", "сентябрь", "жовтня", "октября",
                    "октябрь", "листопада", "ноября", "ноябрь", "грудня", "декабря", "декабрь", "jan", "feb", "mar",
                    "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", "january", "february", "march",
                    "april", "may", "june", "july", "august", "september", "october", "november", "december"),
            wasteSkills = of("продав", "бармен", "ресто", "водитель такси", "виграй"),
            workersIT = of("engineer", "фронт", "front", "бэк", "backend", "web", "веб", "фулстек", "microservice",
                    "микросервис", "програм", "program", "git", "spring", "maven", "develop", "sql", "docker", "postgre",
                    "rest", "mvc", "jpa", "pattern");

    public static boolean isMatch(List<String> area, String text) {
        return area.stream().anyMatch(a -> text.toLowerCase().indexOf(a) != -1);
    }

    public static boolean isMatch(List<String> list1, List<String> list2, List<String> list3, String text) {
        List<String> list = new ArrayList(list1);
        list.addAll(list2);
        list.addAll(list3);
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
