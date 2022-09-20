package ua.training.top.util.collect.data;
import java.util.regex.Pattern;

public class PatternUtil {
    public static Pattern
            pattern_is_date_numbers = Pattern.compile("^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])(t\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})?$"),
            pattern_is_money_value = Pattern.compile(".*[\\d,.]+.*", Pattern.CASE_INSENSITIVE),
            pattern_is_number_greater_300 = Pattern.compile("([3-9]\\d{2}|\\d{4,})(\\.\\d{0,2})?", Pattern.CASE_INSENSITIVE),
            pattern_is_kilo = Pattern.compile(".*\\d[\\d\\.-]+k.*", Pattern.CASE_INSENSITIVE),
            pattern_date_is_numb = Pattern.compile("\\d{1,2}", Pattern.CASE_INSENSITIVE),
            pattern_monetary_amount = Pattern.compile("((?:[\\d,\\.[–до\\-k-  ]+\\s  &nbsp]+\\b)(\\s*)?[  ]?(\\p{Sc}|ƒ))|(" +
                    "(?:\\p{Sc}|ƒ)(\\s*)?[  ]?[\\d,\\.[–до\\-k-  ]\\s  &nbsp]+\\b)", Pattern.CASE_INSENSITIVE),
            pattern_salaries_from_title = Pattern.compile("[\\d,\\.\\sk]++\\s.{0,2}((c(ad?)?\\$)|(cad)|(usd)|(kč)|\\p{Sc})|" +
                    "([\\d,\\.\\sk]++[-\\s]*[\\d,\\.\\sk]++)|((([[\\d,\\.\\sk]\\s-]*)?\\p{Sc}[[\\d,\\.\\sk]\\s-]*)*)", Pattern.CASE_INSENSITIVE),
            pattern_extract_month = Pattern.compile("(?:\\s?\\d?\\d)\\s?\\(?\\s?([\\(месяцева])+\\.*", Pattern.CASE_INSENSITIVE),
            pattern_extract_age = Pattern.compile("(?:[1-7]\\d)\\s([годалетрківи])+", Pattern.CASE_INSENSITIVE),
            pattern_extract_address = Pattern.compile("(?:[а-яА-ЯіїєA-Za-z,\\s·]+)\\b", Pattern.CASE_INSENSITIVE),
            pattern_extract_date = Pattern.compile("((?:\\d){1,2}\\s([а-яі])+|^[а-яі]{3,11})|((?:[A-Za-z])+\\s+(\\d){1,2})", Pattern.CASE_INSENSITIVE),
            pattern_remove_zero_part = Pattern.compile("([\\.,]0{1,2})\\D", Pattern.CASE_INSENSITIVE),
            pattern_date_jobs_bg = Pattern.compile("[1-3]?[0-9][\\.]((0[1-9])|(1[0-2]))[\\.](20)?2[2-9]"),
            pattern_default = Pattern.compile(".*");
}
