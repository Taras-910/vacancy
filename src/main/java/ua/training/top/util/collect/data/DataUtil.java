package ua.training.top.util.collect.data;

import java.time.LocalDate;
import java.util.List;

import static java.util.List.of;
import static ua.training.top.aggregator.installation.InstallationUtil.maxLengthText;

public class DataUtil {
    public static final LocalDate defaultDate = LocalDate.now().minusDays(7);
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
            finish_message = "\ntime: {}ms\nfinish is ok,\ncreated: {}\nupdated: {}\nFreshen: {}\n" + ":".repeat(120),
            error = "There is error \ne={}\n for parse \n{}",
            common_number_vacancyTos = "Common number vacancyTos = {}",
            get_vacancy = "GetVacancies workplace={} language={}",
            error_select = "Select error e {}",
            jobcareer = "JobCareerStrategy", work = "WorkStrategyStrategy", rabota = "RabotaStrategy", djinni = "DjinniStrategy",
            habr = "HabrStrategy", grc = "GrcStrategy", nofluff = "NofluffjobsStrategy", indeed = "UAIndeedStrategy",
            jooble = "jooble", jobsmarket = "jobsmarket", jobs = "jobs", linkedin = "linkedin",
            local_date = "releaseDate", age_field = "age", address_field = "address", month = "month", middle = "middle",
            trainee = "trainee", junior = "junior", senior = "senior", expert = "expert";

    public static final List<String>
            usdAria = of("usd", "$"),
            eurAria = of("eur", "€"),
            plnAria = of("pln", "zł"),
            gbrAria = of("gbp", "£", "₤"),
            kztAria = of("kzt", "тг", "₸"),
            hrnAria = of("hrn", "uah", "грн.", "грн", "₴"),
            rubAria = of("rub", "rur", "руб.", "руб", "₽"),
            bynAria = of("бел. руб.", "бел. руб", "бел руб.", "бел руб", "br", "byn", "byr"),
            allSalaries = of("грн", "uah", "hrn", "₴", "$", "usd", "eur", "€", "pln", "zł", "gbp", "£", "₤", "бел. руб",
                    "бел руб", "руб", "₽", "kzt", "тг", "₸", "br", "byn"),
            yearAria = of("год", "рік", "year"),
            dayAria = of("день", "day"),
            hourAria = of("час", "годину", "hour"),
            wasteSalary = of(" ", " ", "&nbsp;", "[.]{2,}", "(\\p{Sc}|ƒ)", "\\s+"),
            citiesUA = of("ukraine", "украина", "україна", "kyiv", "kiev", "київ", "киев", "дніпро", "днепр", "dnipro",
                    "харків", "харьков", "kharkiv", "львів", "львов", "lviv", "mykolaiv", "одесса", "odesa", "одеса",
                    "винница", "vinnitsia", "вінниця", "запоріжжя", "запорожье", "zaporizhzhya", "chernivtsi", "чернівці",
                    "черновцы", "чернігів", "чернигов", "chernigiv", "івано-франківськ", "ужгород", "ивано-франковск",
                    "ivano-frankivsk", "луцк", "луцьк", "миколаїв", "николаев", "жовті-води", "кривой-рог", "кривий-ріг",
                    "желтые-воды", "каменец-подольский", "камянець-подільський", "тернопіль", "тернополь", "кропивницкий",
                    "кропивницький", "кировоград", "кіровоград"),
            citiesRU = of("санкт-петербург", "москва", "новосибирск", "екатеринбург", "томск", "краснодар",
                    "пермь", "russia", "ростов-на-дону", "нижний новгород", "казань", "самара", "ульяновск", "воронеж"),
            citiesPL = of("poland", "польща", "польша", "polski", "варшава", "warszawa", "krakow", "краков", "wroclaw",
                    "вроцлав", "gdansk", "гданськ", "гданск", "poznan", "познань", "katowice", "катовіце", "катовице",
                    "lodz", "лодзь"),
            monthsOfYearAria = of("січня", "января", "лютого", "февраля", "березня", "марта", "квітня", "апреля",
                    "травня", "мая", "червня", "июня", "липня", "июля", "серпня", "августа", "вересня", "сентября",
                    "жовтня", "октября", "листопада", "ноября", "грудня", "декабря", "jan", "feb", "mar", "apr", "may",
                    "jun", "jul", "aug", "sep", "oct", "nov", "dec"),
            wasteSkills = of("продав", "бармен", "ресто", "водитель такси", "виграй"),
            workersIT = of("engineer", "фронт", "front", "бэк", "backend", "web", "веб", "фулстек", "microservice",
                    "микросервис", "програм", "program", "git", "spring", "maven", "develop", "sql", "docker", "postgre",
                    "rest", "mvc", "jpa", "pattern");

    public static boolean isMatch(List<String> area, String text) {
        return area.stream().anyMatch(a -> text.toLowerCase().indexOf(a) > -1);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.trim().equals("•");
    }

    public static String getLinkIfEmpty(String text) {
        return isEmpty(text) ? link : text;
    }

    public static String getUpperStart(String text) {
        return !isEmpty(text) && text.length() > 1 ? text.substring(0, 1).toUpperCase().concat(text.substring(1)) : link;
    }

    public static String getReplace(String text, List<String> wasteWords, String replacement) {
        for (String s : wasteWords) {
            text = text.replaceAll(s, replacement).trim();
        }
        return text;
    }

    public static String getToTitle(String title) {
        return isEmpty(title) ? link : getUpperStart(title.replaceAll("Java Script", "JavaScript"));
    }

    public static String getToName(String companyName) {
        return isEmpty(companyName) ? link : companyName.contains(",") ? companyName.split(",")[0].trim() : companyName;
    }

    public static String getToSkills(String skills) {
        if (isEmpty(skills)) {
            return link;
        }
        skills = skills.replaceAll("Java Script", "JavaScript");
        skills = skills.contains("Experience level:") ? skills.substring(skills.indexOf("Experience level:")) : skills;
        int limit = maxLengthText;
        return skills.length() > limit ? skills.substring(0, limit) : skills;
    }
}
