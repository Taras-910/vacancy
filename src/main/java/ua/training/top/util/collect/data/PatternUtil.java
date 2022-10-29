package ua.training.top.util.collect.data;
import java.util.regex.Pattern;

public class PatternUtil {
    public static final Pattern
            pattern_find_salary = Pattern.compile("([\\p{Sc}\\p{Pd}]\\s*\\d[\\p{Sc}\\p{Zs}\\d\\.,\\s]+[k\\p{Zs}]*?)|(\\d[\\d\\.,\\s\\p{Zs}]+[k\\p{Zs}]*?\\s*[\\p{Sc}\\p{Pd}доup])", Pattern.CASE_INSENSITIVE);
    public static final Pattern pattern_date_is_numbers = Pattern.compile("^(\\d{4})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])(t\\d{2}:\\d{2}:\\d{2}\\+\\d{2}:\\d{2})?$");
    public static final Pattern pattern_extract_date = Pattern.compile("((?:\\d){1,2}\\s([а-яі])+|^[а-яі]{3,11})|((?:[A-Za-z])+\\s+(\\d){1,2})", Pattern.CASE_INSENSITIVE);
    public static final Pattern pattern_date_jobs_bg = Pattern.compile("[1-3]?[0-9][\\.]((0[1-9])|(1[0-2]))[\\.](20)?2[2-9]");
    public static final Pattern pattern_is_money_value = Pattern.compile("\\d[\\d\\.,]{2,}(\\b|$|\\p{Sc})");
    public static final Pattern pattern_salary_transform_points = Pattern.compile("\\.\\d{3,}");
    public static final Pattern pattern_is_numb = Pattern.compile("\\d{1,2}");
}
