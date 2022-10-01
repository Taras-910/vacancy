package ua.training.top.util.collect.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static java.util.List.of;
import static ua.training.top.util.collect.data.ConstantsUtil.*;
import static ua.training.top.util.collect.data.HelpUtil.*;
import static ua.training.top.util.collect.data.PatternUtil.pattern_find_salary;
import static ua.training.top.util.collect.data.PatternUtil.pattern_salary_transform_points;

public class SalaryUtil {
    public static final Logger log = LoggerFactory.getLogger(SalaryUtil.class);
    public static final float
            rate_pln_to_usd = 4.75f,
            rate_eur_to_usd = 1.01f,
            rate_gbp_to_usd = 0.86f,
            rate_byn_to_usd = 2.52f,
            rate_hrn_to_usd = 36.91f,
            rate_rub_to_usd = 60.83f,
            rate_kzt_to_usd = 472.31f,
            rate_cad_to_usd = 1.31f,
            rate_cze_to_usd = 24.1f,
            rate_bgn_to_usd = 1.96f,
            usd_one_to_one = 1.0f;

    public static Integer[] getToSalaries(String originText) {            //₵ ₮
        originText = originText.toLowerCase();
        if (isEmpty(originText) || !isMatch(allSalaries, originText)) {
            return new Integer[]{1, 1};
        }
        String code = getCurrencyCode(originText);
        String text = getWithCode(originText, code);
        List<Float> values = getMonetaryAmount(text);
        if (!values.isEmpty()) {
            float period = getPeriod(getShortLength(text, code));
            float rate = getRate(code);
            Integer[] salaries = new Integer[2];
            int amountMin = getAmount(values.get(0), period, rate);
            salaries[0] = values.size() < 2 ? (isFrom(text) ? amountMin : 1) : amountMin;
            salaries[1] = values.size() < 2 ? (isFrom(text) ? 1 : amountMin) : getAmount(values.get(1), period, rate);
            return checkLimitUpAndDown(salaries);
        }
        return new Integer[]{1, 1};
    }

    public static int getAmount(float value, float period, float rate) {
        int amount = 1;
        try {
            amount = (int) (value * period / rate);
        } catch (NumberFormatException e) {
            log.error(error, e, value);
        }
        return amount;
    }

    private static Integer[] checkLimitUpAndDown(Integer[] amount) {
        Integer[] result = new Integer[2];
        boolean exceed = false;
        for (int i = amount.length - 1; i >= 0 ; --i) {
            int a = amount[i];
            if (a > 3000000) {
                exceed = true;
                log.error(wrong_salary_value, a/100);
            }
            result[i] = a < 30000 ? 1 : !exceed ? a : Math.min(a / 12, 5000000);
        }
        return result;
    }


    private static List<Float> getMonetaryAmount(String text) {
        List<Float> list = new ArrayList();
        Matcher m = pattern_find_salary.matcher(text);
        while (m.find()) {
            String s = getReplace(m.group(), of(currency_code, space_code), "");
            list.add(getTransform(s.replaceAll(",", ".")));
        }
        return list;
    }

    private static float getTransform(String text) {
        Matcher m = pattern_salary_transform_points.matcher(text);
        while (m.find()) {
            text = text.replaceFirst("\\.", "");
        }
        String salary = text.replaceAll(punctuation_code, "");
        return  Float.parseFloat(salary) * (isContains(text, "k") ? 100000 : 100);
    }

    public static float getPeriod(String text) {
        return isMatch(yearAria, text) ? 1.0f / 12.0f : isMatch(dayAria, text) ? 22.0f :
                isMatch(hourAria, text) ? 22.0f * 8.0f : 1.0f;
    }

    public static String getCurrencyCode(String text) {
        return isMatch(cadAria, text) ? "₡" : isMatch(usdAria, text) ? "$" : isMatch(hrnAria, text) ?
                "₴" : isMatch(eurAria, text) ? "€" : isMatch(bynAria, text) ? "₱" : isMatch(plnAria, text) ?
                "₲" : isMatch(gbrAria, text) ? "₤" : isMatch(kztAria, text) ? "₸" : isMatch(czeAria, text) ?
                "₭" : isMatch(bgnAria, text) ? "₾" : "";
    }

    public static String getWithCode(String text, String code) {
        return switch (code) {
            case "$" -> getReplace(text, of("usd"), "\\$");
            case "₡" -> getReplace(text, cadAria_regex, code);
            case "₴" -> getReplace(text, hrnAria, code);
            case "€" -> getReplace(text, eurAria, code);
            case "₤" -> getReplace(text, gbrAria, code);
            case "₲" -> getReplace(text, plnAria, code);
            case "₸" -> getReplace(text, kztAria, code);
            case "₱" -> getReplace(text, bynAria, code);
            case "₭" -> getReplace(text, czeAria, code);
            case "₾" -> getReplace(text, bgnAria, code);
            default -> text;
        };
    }

    public static Float getRate(String currencyCode) {
        return switch (currencyCode) {
            case "₴" -> rate_hrn_to_usd;
            case "₲" -> rate_pln_to_usd;
            case "€" -> rate_eur_to_usd;
            case "₤" -> rate_gbp_to_usd;
            case "₽" -> rate_rub_to_usd;
            case "₸" -> rate_kzt_to_usd;
            case "₡" -> rate_cad_to_usd;
            case "₱" -> rate_byn_to_usd;
            case "₾" -> rate_bgn_to_usd;
            case "₭" -> rate_cze_to_usd;
            default -> usd_one_to_one;
        };
    }

    public static boolean isFrom(String originText) {
        return isMatch(of("от", "від", "from"), originText);
    }

    private static String getShortLength(String text, String code) {
        return text.length() < 50 ? text :
                text.substring(Math.max(text.indexOf(code) - 25, 0), Math.min(text.lastIndexOf(code) + 25, text.length()));
    }
}
